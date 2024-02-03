package ua.com.underlake.data

import android.os.Parcelable
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
}