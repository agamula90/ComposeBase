package ua.com.underlake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.com.underlake.ui.AppTheme
import ua.com.underlake.ui.BottomBarItem
import ua.com.underlake.ui.NavDrawerItem
import ua.com.underlake.ui.NavHostWithBottomBar
import ua.com.underlake.ui.NavHostWithNavDrawer
import ua.com.underlake.ui.main.ExclusiveFeatures
import ua.com.underlake.ui.navDrawerRootDestinationTransition
import ua.com.underlake.ui.dog.DogsScreen
import ua.com.underlake.ui.dog.details.DogDetailsScreen
import ua.com.underlake.ui.main.Feature
import ua.com.underlake.ui.main.Greeting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                ) {
                    val features = listOf(Feature("BottomBar"), Feature("SideDrawer"), Feature("None"))
                    var checkedFeature by rememberSaveable { mutableStateOf(Feature("None")) }
                    ExclusiveFeatures(
                        featuresGroup = "Navigation",
                        features = features,
                        checkedFeature = checkedFeature,
                        onFeatureCheckChange = { feature -> checkedFeature = feature }
                    )
                    when (checkedFeature) {
                        Feature("BottomBar") -> MainNavHostWithBottomBar(navController = navController)
                        Feature("SideDrawer") -> MainNavHostWithDrawer(navController = navController)
                        Feature("None") -> MainNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MainNavHostWithDrawer(navController: NavHostController) {
    NavHostWithNavDrawer(
        navDrawerItems = listOf(
            NavDrawerItem("tab1", R.drawable.location, "Tab 1"),
            NavDrawerItem("tab2", R.drawable.airplane, "Tab 2"),
            NavDrawerItem("tab3", R.drawable.anchor, "Tab 3")
        ),
        navController = navController
    ) {
        composable(
            route = "tab1",
            enterTransition = navDrawerRootDestinationTransition()
        ) {
            DogsScreen(navigateDogDetails = { navController.navigate("dog/${it}") })
        }
        composable(
            route = "tab2",
            enterTransition = navDrawerRootDestinationTransition()
        ) {
            Greeting(name = "Tab 2")
        }
        composable(
            route = "tab3",
            enterTransition = navDrawerRootDestinationTransition()
        ) {
            Greeting(name = "Tab 3")
        }

        composable(
            route = "dog/{dogId}",
            arguments = listOf(navArgument("dogId") {
                type = NavType.StringType
            })
        ) {
            DogDetailsScreen()
        }
    }
}

@Composable
fun MainNavHostWithBottomBar(navController: NavHostController) {
    NavHostWithBottomBar(
        navController = navController,
        bottomBarItems = listOf(
            BottomBarItem("tab1", R.drawable.location, "Tab 1"),
            BottomBarItem("tab2", R.drawable.airplane, "Tab 2"),
            BottomBarItem("tab3", R.drawable.anchor, "Tab 3"),
        )
    ) {
        composable(
            route = "tab1"
        ) {
            DogsScreen(navigateDogDetails = { navController.navigate("dog/${it}") })
        }
        composable(
            route = "tab2"
        ) {
            Greeting(name = "Tab 2")
        }
        composable(
            route = "tab3"
        ) {
            Greeting(name = "Tab 3")
        }

        composable(
            route = "dog/{dogId}",
            arguments = listOf(navArgument("dogId") {
                type = NavType.StringType
            })
        ) {
            DogDetailsScreen()
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "dogs") {
        composable(
            route = "dogs"
        ) {
            DogsScreen(navigateDogDetails = { navController.navigate("dog/${it}") })
        }
        composable(
            route = "dog/{dogId}",
            arguments = listOf(navArgument("dogId") {
                type = NavType.StringType
            })
        ) {
            DogDetailsScreen()
        }
    }
}