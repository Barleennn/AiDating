package com.aidating

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aidating.ui.AppNavHost
import com.aidating.ui.theme.AiDatingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AiDatingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavRoot()
                }
            }
        }
    }
}

@Composable
private fun AppNavRoot(modifier: Modifier = Modifier) {
    AppNavHost(modifier)
}
