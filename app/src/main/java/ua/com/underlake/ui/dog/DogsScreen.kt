package ua.com.underlake.ui.dog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import ua.com.underlake.features.dog.DogsViewModel
import ua.com.underlake.ui.FullScreenProgressIndicator

@Composable
fun DogsScreen(viewModel: DogsViewModel = koinViewModel(), navigateDogDetails: (String) -> Unit) {
    val loading by viewModel.loading.collectAsState()
    val dogs by viewModel.dogs.collectAsState()

    when {
        loading -> FullScreenProgressIndicator()
        else -> DogsColumn(dogs = dogs, navigateDogDetails = navigateDogDetails)
    }
}