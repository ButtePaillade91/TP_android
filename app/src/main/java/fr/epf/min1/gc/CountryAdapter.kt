package fr.epf.min1.gc

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class CountryAdapter(
    var countries: List<Country>,
    private val listener: OnItemClickListener,
    private val addFavorite: (Country) -> Unit,
    private val removeFavorite: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val countryName: TextView = itemView.findViewById(R.id.country_name)
        val countryCapital: TextView = itemView.findViewById(R.id.country_capital)
        val countryRegion: TextView = itemView.findViewById(R.id.country_region)
        val countryFlag: ImageView = itemView.findViewById(R.id.country_flag)
        val starFavorite: CheckBox = itemView.findViewById(R.id.star_favorite)

        init {
            itemView.setOnClickListener(this)
            starFavorite.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val country = countries[position]
                    if (isChecked) {
                        addFavorite(country)
                        Toast.makeText(itemView.context, "${country.name.common} a été ajouté aux favoris", Toast.LENGTH_SHORT).show()
                    } else {
                        removeFavorite(country)
                        Toast.makeText(itemView.context, "${country.name.common} a été retiré des favoris", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        sharedPreferences = parent.context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val currentCountry = countries[position]
        holder.countryName.text = currentCountry.name.common
        holder.countryCapital.text = currentCountry.capital?.firstOrNull() ?: "N/A"
        holder.countryRegion.text = currentCountry.region

        val flagResourceId = getFlagResourceId(holder.itemView, currentCountry.name.common)
        Log.d("CountryAdapter", "Resource ID for ${currentCountry.name.common}: $flagResourceId")
        if (flagResourceId != 0) {
            holder.countryFlag.setImageResource(flagResourceId)
        } else {
            holder.countryFlag.setImageResource(R.drawable.default_flag)
            Log.e("CountryAdapter", "Flag not found for ${currentCountry.name.common}, using default.")
        }

        // Mettre à jour l'état de la case à cocher en fonction du statut favori
        holder.starFavorite.isChecked = isFavorite(currentCountry)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setData(newCountries: List<Country>) {
        countries = newCountries
        notifyDataSetChanged()
    }

    private fun getFlagResourceId(view: View, countryName: String): Int {
        val context = view.context
        val resourceName = countryName.lowercase().replace(" ", "_")
        Log.d("CountryAdapter", "Looking up flag for: $resourceName")
        val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        Log.d("CountryAdapter", "Resource ID found: $resourceId for $resourceName")
        return resourceId
    }

    private fun isFavorite(country: Country): Boolean {
        return sharedPreferences.getBoolean(country.name.common, false)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}