package com.aidating.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingProgress(progress: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(Color(0x33FFB6B6))
    ) {
        Box(
            modifier = Modifier
                .width((progress.coerceIn(0f, 1f) * 1f).dp) // width will be overridden by parent fillMaxWidth + weight outside if needed
                .fillMaxWidth(progress)
                .height(4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color(0xFFFFE5E5))
        )
    }
}
