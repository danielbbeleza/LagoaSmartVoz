package com.lagoasmartvoz.app.android.designsystem

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dottedBorder(
    color: Color = Color.Gray,
    width: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    intervals: FloatArray = floatArrayOf(10f, 20f) // dot/gap pattern
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(intervals, 0f)
        )
        drawRoundRect(
            color = color,
            size = Size(size.width, size.height),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = stroke
        )
    }
)