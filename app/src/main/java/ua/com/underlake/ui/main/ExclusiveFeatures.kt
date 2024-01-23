package ua.com.underlake.ui.main

import android.os.Parcelable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ua.com.underlake.ui.AppTheme

@Parcelize
data class Feature(val title: String): Parcelable

@Composable
fun ExclusiveFeatures(
    featuresGroup: String,
    features: List<Feature>,
    checkedFeature: Feature,
    onFeatureCheckChange: (Feature) -> Unit
) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = featuresGroup, modifier = Modifier.padding(bottom = 10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (feature in features) {
                Row(
                    modifier = Modifier
                        .border(1.dp, Color.Gray, CircleShape)
                        .clip(CircleShape)
                        .clickable(onClick = { onFeatureCheckChange(feature) }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = checkedFeature == feature,
                        onClick = { onFeatureCheckChange(feature) })
                    Text(text = feature.title, modifier = Modifier.padding(end = 10.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FeaturesRowPreview() {
    AppTheme {
        val features = listOf(Feature("BottomBar"), Feature("SideDrawer"))
        ExclusiveFeatures(
            featuresGroup = "navigation",
            features = features,
            checkedFeature = features[0],
            onFeatureCheckChange = {}
        )
    }
}