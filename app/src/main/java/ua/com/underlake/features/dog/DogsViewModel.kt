package ua.com.underlake.features.dog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ua.com.underlake.data.Dog
import ua.com.underlake.remote.DogService

private const val API_KEY = "live_oFJke7QCVNQIfYlzsw6PKO2J6bRmaq5hsRVkt2j3b5fH1OEsv0UArdkKnFKZjU7n"
private const val MIN_FETCH_TIME = 5000L

@KoinViewModel
class DogsViewModel(
    val dogService: DogService
): ViewModel() {
    val dogs = MutableStateFlow(listOf<Dog>())
    val loading = MutableStateFlow(false)

    init {
        getDogs()
    }

    private fun getDogs() = viewModelScope.launch(Dispatchers.IO) {
        loading.value = true
        val fetchJob = async(Dispatchers.IO) {
            dogs.value = dogService.getDogs(9).map { Dog(it) }
        }
        val waitJob = async {
            delay(MIN_FETCH_TIME)
        }

        awaitAll(fetchJob, waitJob)

        loading.value = false
    }
}