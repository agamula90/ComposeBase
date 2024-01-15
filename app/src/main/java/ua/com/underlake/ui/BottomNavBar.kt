package ua.com.underlake.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import ua.com.underlake.R
import kotlin.math.sqrt

private val MAX_RIPPLE_RADIUS = 60.dp

data class BottomNavItem(
    val destination: String,
    @DrawableRes val icon: Int,
    val text: String
)

@Composable
fun BottomNavBarDecorated(
    bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem("tab1", R.drawable.location, "Tab 1"),
        BottomNavItem("tab2", R.drawable.airplane, "Tab 2"),
        BottomNavItem("tab3", R.drawable.anchor, "Tab 3"),
    ),
    content: @Composable (String, PaddingValues) -> Unit
) {
    var selectedDestination by rememberSaveable(bottomNavItems) {
        mutableStateOf(bottomNavItems[0].destination)
    }
    Scaffold(bottomBar = {
        BottomNavBar(
            bottomNavItems = bottomNavItems,
            activeDestinationId = selectedDestination,
            onDestinationChange = { selectedDestination = it })
    }) {
        content(selectedDestination, it)
    }
}

@Composable
fun BottomNavBar(
    height: Dp = 54.dp,
    itemColor: Color = MaterialTheme.colorScheme.primary,
    activeItemColor: Color = MaterialTheme.colorScheme.secondary,
    bottomNavItems: List<BottomNavItem>,
    activeDestinationId: String,
    onDestinationChange: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(height)
            .clip(RectangleShape)
    ) {
        val rippleRadius = with(LocalDensity.current) {
            val itemWidth = maxWidth.toPx() / (2 * bottomNavItems.size)
            val rippleHeight = maxHeight.toPx() / 2
            val preferredRippleRadius =
                sqrt(itemWidth * itemWidth + rippleHeight * rippleHeight).toDp()
            min(preferredRippleRadius, MAX_RIPPLE_RADIUS)
        }
        Row {
            for (destination in bottomNavItems) {
                val itemTint = when (destination.destination) {
                    activeDestinationId -> activeItemColor
                    else -> itemColor
                }

                BottomNavBarItem(
                    modifier = Modifier
                        .height(height)
                        .weight(1f),
                    tint = itemTint,
                    destination = destination,
                    rippleRadius = rippleRadius,
                    onDestinationChange = onDestinationChange
                )
            }
        }
    }
}

@Composable
fun BottomNavBarItem(
    modifier: Modifier,
    tint: Color,
    destination: BottomNavItem,
    rippleRadius: Dp,
    onDestinationChange: (String) -> Unit
) {
    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false, radius = rippleRadius),
            onClick = { onDestinationChange(destination.destination) }
        ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = destination.icon),
            contentDescription = "",
            tint = tint
        )
        Text(text = destination.text, color = tint)
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomNavBarPreview() {
    val bottomNavItems = listOf(
        BottomNavItem("tab1", R.drawable.location, "Tab 1"),
        BottomNavItem("tab2", R.drawable.airplane, "Tab 2"),
        BottomNavItem("tab3", R.drawable.anchor, "Tab 3"),
    )
    AppTheme {
        BottomNavBar(
            bottomNavItems = bottomNavItems,
            activeDestinationId = bottomNavItems[0].destination,
            onDestinationChange = {}
        )
    }
}