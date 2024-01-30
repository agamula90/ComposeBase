package ua.com.underlake.ui.main

import android.os.Parcelable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ua.com.underlake.ui.AppTheme

@Parcelize
data class Feature(val title: String) : Parcelable

@Composable
fun ExclusiveFeatures(
    featuresGroup: String,
    features: List<Feature>,
    checkedFeature: Feature,
    onFeatureCheckChange: (Feature) -> Unit
) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = featuresGroup, modifier = Modifier.padding(bottom = 10.dp))
        NoticeableRow(countItems = features.size) {
            val feature = features[it]
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

@Composable
fun NoticeableRow(
    countItems: Int,
    itemContent: @Composable (Int) -> Unit
) {
    Layout(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        measurePolicy = { measurables, constraints ->
            val placeables = arrayOfNulls<Placeable?>(measurables.size)
            val parentWidth = constraints.minWidth
            val measureChildConstraints = Constraints()
            var childrenIntrinsicWidth = 0
            var paddingBetweenChildren = 0
            var isPaddingCalculated = false
            var maxHeight = 0
            var containerWidth = 0
            for ((index, measurable) in measurables.withIndex()) {
                val placeable = measurable.measure(measureChildConstraints).also {
                    placeables[index] = it
                    maxHeight = maxOf(maxHeight, it.height)
                    containerWidth += it.width
                }
                if (isPaddingCalculated) {
                    continue
                }

                when {
                    index == 0 && placeable.width > parentWidth -> {
                        paddingBetweenChildren = 0
                        isPaddingCalculated = true
                    }

                    index == 1 && childrenIntrinsicWidth + placeable.width / 2 > parentWidth -> {
                        paddingBetweenChildren = 0
                        isPaddingCalculated = true
                    }

                    childrenIntrinsicWidth + placeable.width / 2 == parentWidth -> {
                        paddingBetweenChildren = 0
                        isPaddingCalculated = true
                    }

                    childrenIntrinsicWidth + placeable.width / 2 > parentWidth -> {
                        paddingBetweenChildren =
                            (parentWidth - childrenIntrinsicWidth + placeables[index - 1]!!.width / 2) / (index - 1)
                        isPaddingCalculated = true
                    }
                }

                childrenIntrinsicWidth += placeable.width
            }

            if (!isPaddingCalculated) {
                paddingBetweenChildren =
                    (parentWidth - childrenIntrinsicWidth + placeables[placeables.size - 1]!!.width / 2) / (placeables.size - 1)
            }

            containerWidth += (measurables.size - 1) * paddingBetweenChildren

            var nextChildPadding = 0

            layout(containerWidth, maxHeight) {
                placeables.forEach {
                    val placeable = it!!
                    placeable.place(nextChildPadding, (maxHeight - placeable.height) / 2)
                    nextChildPadding += placeable.width + paddingBetweenChildren
                }
            }
        },
        content = {
            for (i in 0 until countItems) {
                itemContent(i)
            }
        })
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