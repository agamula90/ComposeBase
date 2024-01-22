package ua.com.underlake.remote.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Breed(
    val name: String,
    @Json(name = "bred_for")
    val purpose: String,
    val temperament: String
)