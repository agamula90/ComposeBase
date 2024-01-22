package ua.com.underlake.remote.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Dog(
    val id: String,
    @Json(name = "url")
    val imageUrl: String
)