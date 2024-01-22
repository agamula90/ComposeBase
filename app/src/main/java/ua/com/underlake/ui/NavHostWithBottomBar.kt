package ua.com.underlake.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavHostWithBottomBar(
    navController: NavHostController,
    bottomBarItems: List<BottomBarItem>,
    navGraph: NavGraphBuilder.() -> Unit
) {
    var selectedDestination by rememberSaveable(bottomBarItems) {
        mutableStateOf(bottomBarItems[0].destination)
    }
    Scaffold(bottomBar = {
        BottomBar(
            bottomBarItems = bottomBarItems,
            activeDestinationId = selectedDestination,
            onDestinationChange = {
                val previousDestination = selectedDestination
                selectedDestination = it
                navController.navigate(it) {
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
        )
    }) {
        NavHost(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            navController = navController,
            startDestination = bottomBarItems[0].destination
        ) {
            navGraph()
        }
    }
}