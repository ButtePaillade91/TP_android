package fr.epf.min1.gc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min1.gc.ApiService.countryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity(), CountryAdapter.OnItemClickListener {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: CountryAdapter
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var waitText: TextView
    private var allCountries: List<Country> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        searchEditText = findViewById(R.id.search_edit_text)
        searchButton = findViewById(R.id.search_button)
        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view)

        searchResultsAdapter = CountryAdapter(emptyList(), this)

        searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = searchResultsAdapter
        }

        searchButton.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            performSearch(searchTerm)
        }

        fetchAllCountries()
    }

    private fun fetchAllCountries() {
        lifecycleScope.launch {
            try {
                val countries = fetchCountries()
                allCountries = countries
                updateSearchResults(countries)
            } catch (e: Exception) {
                Log.e("HomeActivity", "Error fetching all countries", e)
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
                emptyList()
            }
        }
    }

    private fun filterCountries(searchTerm: String): List<Country> {
        Log.e("msg2", "${allCountries}")
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
}