package ua.com.underlake.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.com.underlake.remote.data.Dog

@Parcelize
class Dog(val id: String, val image: String): Parcelable {

    constructor(dog: Dog): this(dog.id, dog.imageUrl)
}