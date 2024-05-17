package fr.epf.min1.gc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

private const val TAG = "ListClientActivity"

class ListClientActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_client)


        recyclerView =
            findViewById<RecyclerView>(R.id.list_clients_recyclerview)

        recyclerView.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)

//        val clients = Client.generateClients()
//        recyclerView.adapter = ClientAdapter(clients)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_clients, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_client -> {
                startActivity(Intent(this, AddClientActivity::class.java))
            }
            R.id.action_synchro -> {
                synchro()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun synchro() {

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        val clientService = retrofit.create(ClientService::class.java)

        runBlocking {
            val clients = clientService.getClients(30)
            Log.d(TAG, "synchro: ${clients}")
            val clientList = clients.results.map {
                Client(
                    it.name.last, it.name.first,
                    if (it.gender == "male") Gender.MAN else Gender.WOMAN
                )
            }


            val adapter = ClientAdapter(clientList)
            recyclerView.adapter = adapter

        }




    }

}