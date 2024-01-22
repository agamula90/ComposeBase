package ua.com.underlake.features.dog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ua.com.underlake.remote.DogService

private const val MIN_FETCH_TIME = 5000L

@KoinViewModel
class DogDetailsViewModel(
    dogService: DogService,
    handle: SavedStateHandle
): ViewModel() {
    private val dogId = handle.get<String>("dogId")!!
    val loading = MutableStateFlow(false)
    val dog = MutableStateFlow(ua.com.underlake.data.DogDetails())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loading.value = true
            val fetchJob = async {
                dog.value = ua.com.underlake.data.DogDetails(dogService.getDogDetails(dogId))
            }
            val waitJob = async {
                delay(MIN_FETCH_TIME)
            }
            awaitAll(fetchJob, waitJob)
            loading.value = false
        }
    }
}