package ua.com.underlake.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.com.underlake.ui.AppTheme

@Composable
fun FeaturesRow(
    features: List<String>,
    checkedFeature: String,
    onFeatureCheckChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (feature in features) {
            Row(
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .clickable(onClick = { onFeatureCheckChange(feature) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = checkedFeature == feature,
                    onClick = { onFeatureCheckChange(feature) })
                Text(text = feature)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FeaturesRowPreview() {
    AppTheme {
        FeaturesRow(
            features = listOf("bottomNavBar", "navDrawer"),
            checkedFeature = "bottomNavBar",
            onFeatureCheckChange = {}
        )
    }
}