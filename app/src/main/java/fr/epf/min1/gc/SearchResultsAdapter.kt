package fr.epf.min1.gc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultsAdapter(private var results: List<Country>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryNameTextView: TextView = itemView.findViewById(R.id.country_name)
        val countryCapitalTextView: TextView = itemView.findViewById(R.id.country_capital)
        val countryRegionTextView: TextView = itemView.findViewById(R.id.country_region)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = results[position]
        holder.countryNameTextView.text = country.name.common
        holder.countryCapitalTextView.text = country.capital.firstOrNull() ?: "N/A"
        holder.countryRegionTextView.text = country.region
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun setData(data: List<Country>) {
        results = data
        notifyDataSetChanged()
    }
}