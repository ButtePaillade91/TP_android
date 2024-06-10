package fr.epf.min1.gc

import retrofit2.Response
import retrofit2.http.GET

interface CountryService {
    @GET("all")
    suspend fun getAllCountries(): Response<List<Country>>
}