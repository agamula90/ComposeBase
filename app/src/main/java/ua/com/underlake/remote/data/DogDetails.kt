package ua.com.underlake.remote.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DogDetails(
    val url: String,
    val breeds: List<Breed>
)