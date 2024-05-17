package fr.epf.min1.gc

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientService {
    @GET("api")
    suspend fun getClients(@Query("results") nb : Int = 20) : ClientsResult
}

data class ClientsResult(val results: List<User>)
data class User(val gender: String, val name: Name)
data class Name(val last: String, val first: String)
