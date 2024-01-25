package ua.com.underlake.features.dog.places

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ua.com.underlake.data.DogDetails
import ua.com.underlake.remote.DogService

private const val MIN_FETCH_TIME = 5000L
private const val UA_LAT_MIN = 44.0
private const val UA_LAT_MAX = 53.0
private const val UA_LNG_MIN = 22.0
private const val UA_LNG_MAX = 41.0
private val minLatLng = Point.fromLngLat(UA_LNG_MIN, UA_LAT_MIN)
private val maxLatLng = Point.fromLngLat(UA_LNG_MAX, UA_LAT_MAX)
val centerLatLng = Point.fromLngLat((UA_LNG_MIN + UA_LNG_MAX) / 2, (UA_LAT_MIN + UA_LAT_MAX) / 2)

@KoinViewModel
class DogPlacesViewModel(
    dogService: DogService,
    handle: SavedStateHandle
): ViewModel() {
    private val dogId = handle.get<String>("dogId")!!
    val loading = MutableStateFlow(false)
    val dogLatLng = MutableStateFlow(DogDetails().getLatLng(minLatLng, maxLatLng))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loading.value = true
            val fetchJob = async {
                dogLatLng.value = DogDetails(dogService.getDogDetails(dogId)).getLatLng(minLatLng, maxLatLng)
            }
            val waitJob = async {
                delay(MIN_FETCH_TIME)
            }
            awaitAll(fetchJob, waitJob)
            loading.value = false
        }
    }
}