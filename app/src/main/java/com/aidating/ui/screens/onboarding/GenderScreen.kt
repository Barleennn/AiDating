package com.aidating.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aidating.ui.components.GradientButton
import com.aidating.ui.components.OnboardingProgress

@Composable
fun GenderScreen(progress: Float, onNext: (String) -> Unit) {
    val selected = remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingProgress(progress, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text("Пол", textAlign = TextAlign.Center)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Мужской", "Женский").forEach { g ->
                FilterChip(selected = selected.value == g, onClick = { selected.value = g }, label = { Text(g) })
            }
        }
        Spacer(Modifier.weight(1f))
        GradientButton(text = "Далее", modifier = Modifier.fillMaxWidth(), enabled = selected.value.isNotEmpty()) {
            onNext(selected.value)
        }
    }
}
