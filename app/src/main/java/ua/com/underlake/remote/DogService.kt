package ua.com.underlake.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.com.underlake.remote.data.Dog
import ua.com.underlake.remote.data.DogDetails

interface DogService {

    @GET("images/{id}")
    suspend fun getDogDetails(@Path("id") id: String): DogDetails

    @GET("images/search?has_breeds=true")
    suspend fun getDogs(@Query("limit") countDogs: Int): List<Dog>
}