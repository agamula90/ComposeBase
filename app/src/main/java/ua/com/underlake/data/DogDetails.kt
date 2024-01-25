package ua.com.underlake.data

import android.os.Parcelable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize
import ua.com.underlake.remote.data.DogDetails

@Parcelize
class Breed(val name: String = "", val usage: String = "", val temperament: String = ""): Parcelable {
    constructor(breed: ua.com.underlake.remote.data.Breed): this(breed.name, breed.purpose, breed.temperament)
}

@Parcelize
class DogDetails(val image: String = "", val breed: Breed = Breed()): Parcelable {
    constructor(dogDetails: DogDetails) : this(
        image = dogDetails.url,
        breed = when {
            dogDetails.breeds.isEmpty() -> Breed()
            else -> Breed(dogDetails.breeds[0])
        }
    )

    fun getLatLng(minLatLng: Point, maxLatLng: Point): Point {
        var temperamentHash = breed.temperament.hashCode()
        if (temperamentHash < 0) temperamentHash = -temperamentHash
        var purposeHash = breed.usage.hashCode()
        if (purposeHash < 0) purposeHash = -purposeHash

        val latitude = minLatLng.latitude() + temperamentHash.toDouble() / Int.MAX_VALUE * (maxLatLng.latitude() - minLatLng.latitude())
        val longitude = minLatLng.longitude() + purposeHash.toDouble() / Int.MAX_VALUE * (maxLatLng.longitude() - minLatLng.longitude())
        return Point.fromLngLat(longitude, latitude)
    }
}