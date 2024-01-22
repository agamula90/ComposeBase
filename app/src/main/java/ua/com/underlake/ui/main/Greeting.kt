package ua.com.underlake.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.com.underlake.ui.AppTheme

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(Modifier.fillMaxSize()) {
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