package fr.epf.min1.gc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailsClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_client)

        val lastNameTextView = findViewById<TextView>(R.id.details_client_lastname_textview)
        intent.extras?.apply {
            val id = getInt(CLIENT_ID_EXTRA)
            lastNameTextView.text = Client.generateClients()[id].name
            val client = getParcelable(CLIENT_EXTRA) as? Client
            client?.let {
                lastNameTextView.text = it.name

            }
        }
    }
}