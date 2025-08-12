package com.aidating.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aidating.ui.components.GradientButton
import com.aidating.ui.components.OnboardingProgress

@Composable
fun LanguagesScreen(progress: Float, onNext: (List<String>) -> Unit) {
    val options = listOf("Русский", "English", "Deutsch", "Español")
    val selected = remember { mutableStateListOf<String>() }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingProgress(progress, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text("Языки", textAlign = TextAlign.Center)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            options.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    row.forEach { lang ->
                        val isSel = selected.contains(lang)
                        AssistChip(onClick = {
                            if (isSel) selected.remove(lang) else selected.add(lang)
                        }, label = { Text(lang) })
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
        GradientButton(text = "Далее", modifier = Modifier.fillMaxWidth(), enabled = selected.isNotEmpty()) {
            onNext(selected.toList())
        }
    }
}
