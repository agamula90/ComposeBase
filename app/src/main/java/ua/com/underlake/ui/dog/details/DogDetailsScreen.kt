package ua.com.underlake.ui.dog.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ua.com.underlake.features.dog.DogDetailsViewModel
import ua.com.underlake.ui.FullScreenProgressIndicator

@Composable
fun DogDetailsScreen(viewModel: DogDetailsViewModel = koinViewModel()) {
    val loading by viewModel.loading.collectAsState()
    val dog by viewModel.dog.collectAsState()

    when {
        loading -> FullScreenProgressIndicator()
        else -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp)) {
                AsyncImage(
                    model = dog.image,
                    contentDescription = dog.breed.name,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Crop
                )

                Text(text = "Name: ${dog.breed.name}", modifier = Modifier.padding(top = 5.dp))
                Text(text = "Dog usage: ${dog.breed.usage}", modifier = Modifier.padding(top = 5.dp))
                Text(text = "Temperament: ${dog.breed.temperament}", modifier = Modifier.padding(top = 5.dp))
            }
        }
    }
}