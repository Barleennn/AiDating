package com.aidating.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
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
fun AboutScreen(progress: Float, onNext: (String) -> Unit) {
    val about = remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingProgress(progress, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text("О себе", textAlign = TextAlign.Center)
        OutlinedTextField(
            value = about.value,
            onValueChange = { about.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("О себе") }
        )
        Spacer(Modifier.weight(1f))
        GradientButton(text = "Далее", modifier = Modifier.fillMaxWidth()) {
            onNext(about.value)
        }
    }
}
