package com.aidating.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val name = remember { mutableStateOf("Alex") }
    val about = remember { mutableStateOf("Люблю путешествия и кофе") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Профиль")
        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Имя") })
        OutlinedTextField(value = about.value, onValueChange = { about.value = it }, label = { Text("О себе") })
        Button(onClick = onBack) { Text("Назад") }
    }
}
