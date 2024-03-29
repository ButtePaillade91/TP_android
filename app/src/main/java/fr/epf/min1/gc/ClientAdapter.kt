package fr.epf.min1.gc

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ClientViewHolder(view: View) : RecyclerView.ViewHolder(view)


const val CLIENT_ID_EXTRA = "clientid"

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

//        textView.text = "${client.firstname} ${client.lastname}"
            textView.text = client.name

        val imageView = view.findViewById<ImageView>(R.id.client_view_imageview)

        imageView.setImageResource(client.getImage())
//        imageView.setImageResource(
//            when(client.gender){
//                Gender.MAN -> R.drawable.man
//                Gender.WOMAN -> R.drawable.woman
//            }
//            if(client.gender == Gender.MAN) R.drawable.man else R.drawable.woman
//        )

        val cardview = view.findViewById<CardView>(R.id.client_view_cardview)
        cardview.click {
//            val context = it.context
//            val intent = Intent(context, DetailsClientActivity::class.java)
//            context.startActivity(intent)
            with(it.context){
                val intent = Intent(this, DetailsClientActivity::class.java)
                intent.putExtra(CLIENT_ID_EXTRA, position)
                startActivity(intent)
            }
        }
    }
}



