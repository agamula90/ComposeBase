package ua.com.underlake.ui.dog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ua.com.underlake.data.Dog

@Composable
fun DogsColumn(
    dogs: List<Dog>,
    navigateDogDetails: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 10.dp)) {
        itemsIndexed(dogs, key = { index, item -> item.id }) { index, item ->
            AsyncImage(
                model = item.image,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(130.dp, 70.dp).clickable { navigateDogDetails(item.id) }
            )
            if (index != dogs.size - 1) Spacer(Modifier.height(10.dp))
        }
    }
}