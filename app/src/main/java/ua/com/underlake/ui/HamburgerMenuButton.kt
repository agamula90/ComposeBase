package ua.com.underlake.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

@Composable
fun HamburgerMenuButton(
    collapsed: Boolean,
    expansionProgress: Float,
    onCollapseChange: (Boolean) -> Unit
) {
    val backIcon = remember { provideBackIcon() }
    val menuIcon = remember { provideMenuIcon() }
    IconToggleButton(checked = collapsed, onCheckedChange = onCollapseChange) {
        Icon(
            painter = rememberMorphPainter(menuIcon, backIcon, expansionProgress),
            contentDescription = "Menu",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

private fun provideBackIcon(): ImageVector = imageVector("backIcon") {
    path {
        moveTo(3f, 12f)
        lineToRelative(8f, 8f)
        lineToRelative(1.4f, -1.4f)
        lineTo(4f, 11f)
        lineTo(3f, 12f)
        close()
        moveTo(4f, 13f)
        lineToRelative(17f, 0f)
        lineToRelative(0f, -2f)
        lineTo(4f, 11f)
        lineTo(4f, 13f)
        close()
        moveTo(3f, 12f)
        lineToRelative(1f, 1f)
        lineToRelative(8f, -8f)
        lineTo(11f, 4f)
        lineTo(3f, 12f)
        close()
    }
}

private fun provideMenuIcon(): ImageVector = imageVector("menuIcon") {
    path {
        moveTo(3f, 18f)
        lineToRelative(18f, 0f)
        lineToRelative(0f, -2f)
        lineTo(3f, 16f)
        lineTo(3f, 18f)
        close()
        moveTo(3f, 13f)
        lineToRelative(18f, 0f)
        lineToRelative(0f, -2f)
        lineTo(3f, 11f)
        lineTo(3f, 13f)
        close()
        moveTo(3f, 6f)
        lineToRelative(0f, 2f)
        lineToRelative(18f, 0f)
        lineTo(21f, 6f)
        lineTo(3f, 6f)
        close()
    }
}