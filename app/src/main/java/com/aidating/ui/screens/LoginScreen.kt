package com.aidating.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoggedIn: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Title from Figma
        Text(
            text = "Открой для себя новые знакомства с AI",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0A0A0A),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Коллаж из трёх карточек с лёгкими поворотами
        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = com.aidating.R.drawable.login_collage_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .rotate(-10f)
            )
            Image(
                painter = painterResource(id = com.aidating.R.drawable.login_collage_2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Image(
                painter = painterResource(id = com.aidating.R.drawable.login_collage_3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .rotate(10f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        GoogleSignInButton(
            text = "Войти с помощью Google",
            onClick = onLoggedIn,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Политика конфиденциальности",
            color = Color(0xFF007AFF),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Нижний индикатор (Home Indicator)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color(0xFF0A0A0A).copy(alpha = 0.9f))
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun GoogleSignInButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF62A3F3), Color(0xFF007AFF))
    )
    Box(
        modifier
            .clip(CircleShape)
            .background(gradient)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Icon placeholder (white circle with G)
            Box(
                modifier = Modifier.clip(CircleShape).background(Color.White).padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("G", color = Color(0xFF4285F4))
            }
            Text(text, color = Color.White, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}
