package fr.epf.min1.gc

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.epf.min1.gc.ApiService.countryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity(), CountryAdapter.OnItemClickListener {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: CountryAdapter
    private var allCountries: List<Country> = emptyList()
    private var favoriteCountries: MutableList<Country> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialisation des vues
        searchEditText = findViewById(R.id.search_edit_text)
        searchButton = findViewById(R.id.search_button)
        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view)

        // Configuration de la RecyclerView
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        searchResultsAdapter = CountryAdapter(emptyList(), this, ::addCountryToFavorites, ::removeCountryFromFavorites)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        // Gestion du clic sur le bouton de recherche
        searchButton.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            performSearch(searchTerm)
        }

        // Charger les données appropriées en fonction de la disponibilité du réseau
        if (isNetworkAvailable()) {
            // Si le réseau est disponible, charger tous les pays
            fetchAllCountries()
        } else {
            // Si le réseau n'est pas disponible, charger les pays favoris
            val favoriteCountries = getFavoriteCountries()
            if (favoriteCountries.isNotEmpty()) {
                updateSearchResults(favoriteCountries)
            } else {
                // Afficher un message indiquant qu'aucun favori n'est disponible
                Toast.makeText(this, "Aucun pays favori n'est disponible.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAllCountries() {
        if (isNetworkAvailable()) {
            lifecycleScope.launch {
                try {
                    val countries = fetchCountries()
                    allCountries = countries
                    updateSearchResults(countries)
                } catch (e: Exception) {
                    Log.e("HomeActivity", "Error fetching all countries", e)
                }
            }
        } else {
            val favoriteCountries = getFavoriteCountries()
            if (favoriteCountries.isNotEmpty()) {
                allCountries = favoriteCountries
                updateSearchResults(favoriteCountries)
                Toast.makeText(this, "Affichage des pays favoris", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Pas de connexion réseau et aucun favori enregistré", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSearch(searchTerm: String) {
        if (allCountries.isNotEmpty()) {
            val filteredCountries = filterCountries(searchTerm)
            updateSearchResults(filteredCountries)
            searchResultsRecyclerView.visibility = View.VISIBLE
        } else {
            Log.e("HomeActivity", "List of countries is empty")
        }
    }

    private suspend fun fetchCountries(): List<Country> {
        return withContext(Dispatchers.IO) {
            try {
                val response = countryService.getAllCountries()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    Log.e("fetchCountries", "Error fetching countries: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("fetchCountries", "Error fetching countries", e)
                fetchAllCountries()
                emptyList()
            }
        }
    }

    private fun filterCountries(searchTerm: String): List<Country> {
        return allCountries.filter { country ->
            country.name.common.contains(searchTerm, ignoreCase = true) ||
                    (country.capital != null && country.capital.any { it.contains(searchTerm, ignoreCase = true) }) ||
                    country.region.contains(searchTerm, ignoreCase = true)
        }
    }

    private fun updateSearchResults(countries: List<Country>) {
        searchResultsAdapter.setData(countries)
    }

    override fun onItemClick(position: Int) {
        val clickedCountry = searchResultsAdapter.countries[position]
        val intent = Intent(this, CountryDetailActivity::class.java).apply {
            putExtra("EXTRA_NAME", clickedCountry.name.common)
            putExtra("EXTRA_CAPITAL", clickedCountry.capital.firstOrNull() ?: "N/A")
            putExtra("EXTRA_REGION", clickedCountry.region)
            putExtra("EXTRA_FLAG_URL", clickedCountry.flags.png)
        }
        startActivity(intent)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun addCountryToFavorites(country: Country) {
        val favorites = getFavoriteCountries().toMutableList()
        if (!favorites.contains(country)) {
            favorites.add(country)
            saveFavoriteCountries(favorites)
            // Mettre à jour l'affichage après l'ajout d'un favori
            updateSearchResults(favorites)
        }
    }

    private fun removeCountryFromFavorites(country: Country) {
        val favorites = getFavoriteCountries().toMutableList()
        favorites.remove(country)
        saveFavoriteCountries(favorites)
    }

    private fun saveFavoriteCountries(favoriteCountries: List<Country>) {
        val favoritesJson = Gson().toJson(favoriteCountries)
        val sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("favorite_countries", favoritesJson).apply()
    }

    private fun getFavoriteCountries(): List<Country> {
        val sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoritesJson = sharedPreferences.getString("favorite_countries", "[]")
        return Gson().fromJson(favoritesJson, Array<Country>::class.java).toList()
    }
}