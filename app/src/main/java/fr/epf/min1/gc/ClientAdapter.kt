package fr.epf.min1.gc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClientViewHolder(view: View) : RecyclerView.ViewHolder(view)


class ClientAdapter(val clients: List<Client>)
    : RecyclerView.Adapter<ClientViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view  = layoutInflater.inflate(R.layout.client_view,parent,false)
        return ClientViewHolder(view)
    }

    override fun getItemCount() = clients.size

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        val view = holder.itemView
        val textView =
            view.findViewById<TextView>(R.id.client_view_name_textview)

        textView.text = "${client.firstname} ${client.lastname}"
    }
}