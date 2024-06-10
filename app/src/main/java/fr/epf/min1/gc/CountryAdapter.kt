package fr.epf.min1.gc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class CountryAdapter(
    var countries: List<Country>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val countryName: TextView = itemView.findViewById(R.id.country_name)
        val countryCapital: TextView = itemView.findViewById(R.id.country_capital)
        val countryRegion: TextView = itemView.findViewById(R.id.country_region)
        val countryFlag: ImageView = itemView.findViewById(R.id.country_flag)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val currentCountry = countries[position]
        holder.countryName.text = currentCountry.name.common
        holder.countryCapital.text = currentCountry.capital.firstOrNull() ?: "N/A"
        holder.countryRegion.text = currentCountry.region

        val flagResourceId = getFlagResourceId(holder.itemView, currentCountry.name.common)
        Log.d("CountryAdapter", "Resource ID for ${currentCountry.name.common}: $flagResourceId")
        if (flagResourceId != 0) {
            holder.countryFlag.setImageResource(flagResourceId)
        } else {
            holder.countryFlag.setImageResource(R.drawable.default_flag) // Image par défaut si le drapeau n'est pas trouvé
            Log.e("CountryAdapter", "Flag not found for ${currentCountry.name.common}, using default.")
        }
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}