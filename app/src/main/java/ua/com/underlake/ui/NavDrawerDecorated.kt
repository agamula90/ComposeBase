package ua.com.underlake.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.com.underlake.R

data class NavDrawerItem(
    val destination: String,
    @DrawableRes val icon: Int,
    val text: String,
)

private val DRAWER_WIDTH = 360.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawerDecorated(
    navDrawerItems: List<NavDrawerItem> = listOf(
        NavDrawerItem("tab1", R.drawable.location, "Tab 1"),
        NavDrawerItem("tab2", R.drawable.airplane, "Tab 2"),
        NavDrawerItem("tab3", R.drawable.anchor, "Tab 3")
    ),
    navDrawerItemIcon: @Composable (NavDrawerItem, Color) -> Unit,
    navDrawerItemLabel: @Composable (NavDrawerItem, Color) -> Unit,
    content: @Composable (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerWidth = with(LocalDensity.current) { DRAWER_WIDTH.toPx() }
    val expansionOffset by drawerState.offset
    val expansionProgress = -expansionOffset / drawerWidth
    val coroutineScope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(text = "title")
        },
        navigationIcon = {
            HamburgerMenuButton(
                collapsed = !drawerState.isOpen,
                expansionProgress = expansionProgress,
                onCollapseChange = {
                    coroutineScope.launch {
                        val targetState = when {
                            drawerState.isOpen -> DrawerValue.Closed
                            else -> DrawerValue.Open
                        }
                        drawerState.animateTo(targetState, tween(700))
                    }
                }
            )
        }
    )

    var selectedDestination by rememberSaveable(navDrawerItems) {
        mutableStateOf(navDrawerItems[0].destination)
    }

    ModalNavigationDrawer(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = MaterialTheme.shapes.small,
                drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
                drawerContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                drawerTonalElevation = 4.dp,
            ) {
                navDrawerItems.forEach { navDrawerItem ->
                    NavigationDrawerItem(
                        selected = selectedDestination == navDrawerItem.destination,
                        onClick = {
                            selectedDestination = navDrawerItem.destination
                            coroutineScope.launch { drawerState.close() }
                        },
                        icon = {
                            val colorTint = if (selectedDestination == navDrawerItem.destination) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onTertiaryContainer
                            }

                            navDrawerItemIcon(
                                navDrawerItem,
                                colorTint
                            )
                        },
                        label = {
                            val colorTint = if (selectedDestination == navDrawerItem.destination) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onTertiaryContainer
                            }

                            navDrawerItemLabel(
                                navDrawerItem,
                                colorTint
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    )
                }
            }
        },
        content = {
            content(selectedDestination)
        })
}