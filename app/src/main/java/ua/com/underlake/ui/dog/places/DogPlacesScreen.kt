package ua.com.underlake.ui.dog.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.annotationAnchor
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import org.koin.androidx.compose.koinViewModel
import ua.com.underlake.R
import ua.com.underlake.features.dog.places.DogPlacesViewModel
import ua.com.underlake.features.dog.places.centerLatLng
import ua.com.underlake.ui.FullScreenProgressIndicator

@OptIn(MapboxExperimental::class)
@Composable
fun DogPlacesScreen(viewModel: DogPlacesViewModel = koinViewModel()) {
    val loading by viewModel.loading.collectAsState()
    val point by viewModel.dogLatLng.collectAsState()

    when {
        loading -> FullScreenProgressIndicator()
        else -> {
            val mapState = rememberMapViewportState {
                setCameraOptions {
                    zoom(4.0)
                    center(centerLatLng)
                    pitch(0.0)
                    bearing(0.0)
                }
            }
            MapboxMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp),
                mapViewportState = mapState
            ) {
                ViewAnnotation(options = viewAnnotationOptions {
                    geometry(point)
                    annotationAnchor { 
                        anchor(ViewAnnotationAnchor.CENTER)
                    }
                    allowOverlap(false)
                }) {
                    Image(
                        painter = painterResource(R.drawable.anchor),
                        modifier = Modifier.size(40.dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}