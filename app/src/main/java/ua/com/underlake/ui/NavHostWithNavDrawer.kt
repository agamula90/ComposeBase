package ua.com.underlake.ui

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
class NavDrawerItem(
    val destination: String,
    @DrawableRes val icon: Int,
    val text: String,
): Parcelable

private val DRAWER_WIDTH = 360.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHostWithNavDrawer(
    navController: NavHostController,
    navDrawerItems: List<NavDrawerItem>,
    navGraph: NavGraphBuilder.() -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerWidth = with(LocalDensity.current) { DRAWER_WIDTH.toPx() }
    val firstDrawPass = remember { mutableStateOf(true) }
    val expansionProgress by remember {
        derivedStateOf {
            val offset = when {
                firstDrawPass.value && drawerState.offset.value == 0.0f -> -drawerWidth
                else -> drawerState.offset.value
            }
            val value = (-offset / drawerWidth * 20).toInt()
            value / 20f
        }
    }

    Column {
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
            mutableStateOf(navDrawerItems[0])
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
                            selected = selectedDestination == navDrawerItem,
                            onClick = {
                                val previousDestination = selectedDestination.destination
                                selectedDestination = navDrawerItem
                                coroutineScope.launch {
                                    drawerState.animateTo(DrawerValue.Closed, tween(700))
                                    navController.navigate(navDrawerItem.destination) {
                                        popUpTo(previousDestination) {
                                            saveState = true
                                            inclusive = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                val colorTint = if (selectedDestination == navDrawerItem) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onTertiaryContainer
                                }

                                Icon(
                                    painter = painterResource(id = navDrawerItem.icon),
                                    contentDescription = "",
                                    tint = colorTint
                                )
                            },
                            label = {
                                val colorTint = if (selectedDestination == navDrawerItem) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onTertiaryContainer
                                }

                                Text(
                                    text = navDrawerItem.text,
                                    color = colorTint
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
                NavHost(
                    navController = navController,
                    startDestination = navDrawerItems[0].destination,
                    exitTransition = {
                        fadeOut(animationSpec = tween(400))
                    }
                ) {
                    navGraph()
                }
            })
    }

    SideEffect {
        firstDrawPass.value = false
    }
}

fun navDrawerRootDestinationTransition():  AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
    return {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(1000)
        )
    }
}