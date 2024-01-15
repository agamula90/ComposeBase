package ua.com.underlake.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

private const val HAMBURGER_ANIMATION_DURATION_MILLIS = 500

@Composable
fun HamburgerMenuButton(collapsed: Boolean, onCollapseChange: (Boolean) -> Unit) {
    val backIcon = remember { provideBackIcon() }
    val menuIcon = remember { provideMenuIcon() }
    IconToggleButton(checked = collapsed, onCheckedChange = onCollapseChange) {
        Icon(
            painter = rememberMorphPainter(menuIcon, backIcon, collapsed),
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

private fun imageVector(
    name: String,
    tintColor: Color = Color.White,
    block: ImageVector.Builder.() -> ImageVector.Builder
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
    tintColor = tintColor
).apply { block() }.build()

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

@Composable
fun rememberMorphPainter(
    checkedVector: ImageVector,
    uncheckedVector: ImageVector,
    checked: Boolean
): Painter {
    return rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        name = "morphPainter",
        autoMirror = false
    ) { _, _ ->
        val transition = updateTransition(targetState = checked, label = "CheckClose")
        val fraction by transition.animateFloat(
            label = "PathNodes",
            transitionSpec = { tween(durationMillis = HAMBURGER_ANIMATION_DURATION_MILLIS) }
        ) { state -> if (state) 1f else 0f }

        val rotation by transition.animateFloat(
            label = "rotation",
            transitionSpec = { tween(durationMillis = HAMBURGER_ANIMATION_DURATION_MILLIS) }
        ) { state -> if (state) 180f else 0f }

        val checkedPathData = remember(checkedVector) {
            checkedVector.getPathData()
        }
        val uncheckedPathData = remember(uncheckedVector) {
            uncheckedVector.getPathData()
        }
        val pathNodes = when {
            canMorph(uncheckedPathData, checkedPathData) -> lerp(uncheckedPathData, checkedPathData, fraction)
            fraction > 0.5f -> checkedPathData
            else -> uncheckedPathData
        }
        val pathRotation = when {
            canMorph(uncheckedPathData, checkedPathData) -> rotation
            else -> 0f
        }

        Group(
            name = "morphRoot",
            translationX = 0.0f,
            translationY = 0.0f,
            pivotX = 12.0f,
            pivotY = 12.0f,
            rotation = pathRotation
        ) {
            Path(
                pathData = pathNodes,
                stroke = SolidColor(Color.White),
                strokeAlpha = 1f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Square,
            )
        }
    }
}

private fun ImageVector.getPathData(): List<PathNode> {
    val members = VectorGroup::class.java
    val field = members.getDeclaredField("children")
    field.isAccessible = true
    val childrenMember = field.get(root) as List<VectorPath>
    return childrenMember[0].pathData
}

// Paths can morph if same size and same node types at same positions.
fun canMorph(from: List<PathNode>, to: List<PathNode>): Boolean {
    if (from.size != to.size) {
        return false
    }

    for (i in from.indices) {
        if (from[i].javaClass != to[i].javaClass) {
            return false
        }
    }

    return true
}

// Assume paths can morph (see [canMorph]). If not, will throw.
private fun lerp(
    fromPath: List<PathNode>,
    toPath: List<PathNode>,
    fraction: Float
): List<PathNode> {
    return fromPath.mapIndexed { i, from ->
        val to = toPath[i]
        lerp(from, to, fraction)
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

private fun lerp(from: PathNode, to: PathNode, fraction: Float): PathNode {
    return when (from) {
        PathNode.Close -> {
            to as PathNode.Close
            from
        }

        is PathNode.RelativeMoveTo -> {
            to as PathNode.RelativeMoveTo
            PathNode.RelativeMoveTo(
                lerp(from.dx, to.dx, fraction),
                lerp(from.dy, to.dy, fraction),
            )
        }

        is PathNode.MoveTo -> {
            to as PathNode.MoveTo
            PathNode.MoveTo(
                lerp(from.x, to.x, fraction),
                lerp(from.y, to.y, fraction),
            )
        }

        is PathNode.RelativeLineTo -> {
            to as PathNode.RelativeLineTo
            PathNode.RelativeLineTo(
                lerp(from.dx, to.dx, fraction),
                lerp(from.dy, to.dy, fraction),
            )
        }

        is PathNode.LineTo -> {
            to as PathNode.LineTo
            PathNode.LineTo(
                lerp(from.x, to.x, fraction),
                lerp(from.y, to.y, fraction),
            )
        }

        is PathNode.RelativeHorizontalTo -> {
            to as PathNode.RelativeHorizontalTo
            PathNode.RelativeHorizontalTo(
                lerp(from.dx, to.dx, fraction)
            )
        }

        is PathNode.HorizontalTo -> {
            to as PathNode.HorizontalTo
            PathNode.HorizontalTo(
                lerp(from.x, to.x, fraction)
            )
        }

        is PathNode.RelativeVerticalTo -> {
            to as PathNode.RelativeVerticalTo
            PathNode.RelativeVerticalTo(
                lerp(from.dy, to.dy, fraction)
            )
        }

        is PathNode.VerticalTo -> {
            to as PathNode.VerticalTo
            PathNode.VerticalTo(
                lerp(from.y, to.y, fraction)
            )
        }

        is PathNode.RelativeCurveTo -> {
            to as PathNode.RelativeCurveTo
            PathNode.RelativeCurveTo(
                lerp(from.dx1, to.dx1, fraction),
                lerp(from.dy1, to.dy1, fraction),
                lerp(from.dx2, to.dx2, fraction),
                lerp(from.dy2, to.dy2, fraction),
                lerp(from.dx3, to.dx3, fraction),
                lerp(from.dy3, to.dy3, fraction),
            )
        }

        is PathNode.CurveTo -> {
            to as PathNode.CurveTo
            PathNode.CurveTo(
                lerp(from.x1, to.x1, fraction),
                lerp(from.y1, to.y1, fraction),
                lerp(from.x2, to.x2, fraction),
                lerp(from.y2, to.y2, fraction),
                lerp(from.x3, to.x3, fraction),
                lerp(from.y3, to.y3, fraction),
            )
        }

        is PathNode.RelativeReflectiveCurveTo -> {
            to as PathNode.RelativeReflectiveCurveTo
            PathNode.RelativeReflectiveCurveTo(
                lerp(from.dx1, to.dx1, fraction),
                lerp(from.dy1, to.dy1, fraction),
                lerp(from.dx2, to.dx2, fraction),
                lerp(from.dy2, to.dy2, fraction),
            )
        }

        is PathNode.ReflectiveCurveTo -> {
            to as PathNode.ReflectiveCurveTo
            PathNode.ReflectiveCurveTo(
                lerp(from.x1, to.x1, fraction),
                lerp(from.y1, to.y1, fraction),
                lerp(from.x2, to.x2, fraction),
                lerp(from.y2, to.y2, fraction),
            )
        }

        is PathNode.RelativeQuadTo -> {
            to as PathNode.RelativeQuadTo
            PathNode.RelativeQuadTo(
                lerp(from.dx1, to.dx1, fraction),
                lerp(from.dy1, to.dy1, fraction),
                lerp(from.dx2, to.dx2, fraction),
                lerp(from.dy2, to.dy2, fraction),
            )
        }

        is PathNode.QuadTo -> {
            to as PathNode.QuadTo
            PathNode.QuadTo(
                lerp(from.x1, to.x1, fraction),
                lerp(from.y1, to.y1, fraction),
                lerp(from.x2, to.x2, fraction),
                lerp(from.y2, to.y2, fraction),
            )
        }

        is PathNode.RelativeReflectiveQuadTo -> {
            to as PathNode.RelativeReflectiveQuadTo
            PathNode.RelativeReflectiveQuadTo(
                lerp(from.dx, to.dx, fraction),
                lerp(from.dy, to.dy, fraction),
            )
        }

        is PathNode.ReflectiveQuadTo -> {
            to as PathNode.ReflectiveQuadTo
            PathNode.ReflectiveQuadTo(
                lerp(from.x, to.x, fraction),
                lerp(from.y, to.y, fraction),
            )
        }

        is PathNode.RelativeArcTo -> TODO("Support for RelativeArcTo not implemented yet")
        is PathNode.ArcTo -> TODO("Support for ArcTo not implemented yet")
    }
}