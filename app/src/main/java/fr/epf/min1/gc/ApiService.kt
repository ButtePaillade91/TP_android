package fr.epf.min1.gc

object ApiService {
    val countryService: CountryService by lazy {
        RetrofitClient.retrofit.create(CountryService::class.java)
    }
}
