package fr.epf.min1.gc

import com.squareup.moshi.Json

data class Name(
    @Json(name = "common") val common: String,
    @Json(name = "official") val official: String
)

data class Country(
    @Json(name = "name") val name: Name,
    @Json(name = "capital") val capital: List<String>,
    @Json(name = "region") val region: String,
    @Json(name = "flags") val flags: Flags
)

data class Flags(
    @Json(name = "png") val png: String
)
