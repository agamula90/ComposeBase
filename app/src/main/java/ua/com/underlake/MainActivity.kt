package ua.com.underlake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ua.com.underlake.ui.AppTheme
import ua.com.underlake.ui.BottomNavBarDecorated
import ua.com.underlake.ui.NavDrawerDecorated
import ua.com.underlake.ui.helper.FeaturesRow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            AppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    val features = listOf("bottomNavBar", "navDrawer")
                    var checkedFeature by rememberSaveable { mutableStateOf(features[0]) }
                    FeaturesRow(
                        features = features,
                        checkedFeature = checkedFeature,
                        onFeatureCheckChange = { feature -> checkedFeature = feature }
                    )
                    when (checkedFeature) {
                        "bottomNavBar" -> {
                            BottomNavBarDecorated { destinationId, paddingValues ->
                                Greeting(name = destinationId)
                            }
                        }

                        "navDrawer" -> {
                            NavDrawerDecorated(
                                navDrawerItemIcon = { item, colorTint ->
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = "",
                                        tint = colorTint
                                    )
                                },
                                navDrawerItemLabel = { item, colorTint ->
                                    Text(
                                        text = item.text,
                                        color = colorTint
                                    )
                                },
                                content = {
                                    Greeting(name = it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        var checked by remember { mutableStateOf(true) }
        val thumbContent: @Composable (() -> Unit)? = when {
            checked -> {
                {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "checked"
                    )
                }
            }
            else -> null
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            thumbContent = thumbContent
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}