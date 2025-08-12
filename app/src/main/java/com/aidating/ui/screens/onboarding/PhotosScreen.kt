package com.aidating.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aidating.ui.components.GradientButton
import com.aidating.ui.components.OnboardingProgress

@Composable
fun PhotosScreen(progress: Float, onFinish: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Учитываем горизонтальные отступы экрана (16 + 16) и промежутки между фото (12 + 12)
    val photoWidth = (screenWidth - 32.dp - 24.dp) / 3

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingProgress(progress, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text("Фотографии", textAlign = TextAlign.Center)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SamplePhoto(resId = com.aidating.R.drawable.login_collage_1, width = photoWidth)
                SamplePhoto(resId = com.aidating.R.drawable.login_collage_2, width = photoWidth)
                SamplePhoto(resId = com.aidating.R.drawable.login_collage_3, width = photoWidth)
            }
        }
        GradientButton(text = "Готово", modifier = Modifier.fillMaxWidth()) { onFinish() }
    }
}

@Composable
private fun SamplePhoto(resId: Int, width: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEFF3FF)),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = resId), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.matchParentSize())
    }
}
