package com.yoinks.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.yoinks.app.ui.screens.HomeScreen
import com.yoinks.app.ui.theme.DarkBackground
import com.yoinks.app.ui.theme.YoinksTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Extraer enlace si viene de "Compartir" de otra app (YouTube, TikTok, Instagram, etc.)
        val sharedUrl = extractSharedUrl(intent)

        setContent {
            YoinksTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    HomeScreen(initialSharedUrl = sharedUrl)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val sharedUrl = extractSharedUrl(intent)
        setContent {
            YoinksTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    HomeScreen(initialSharedUrl = sharedUrl)
                }
            }
        }
    }

    private fun extractSharedUrl(intent: Intent?): String? {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
            // Extrae la primera URL válida del texto compartido
            val urlRegex = Regex("https?://[^\\s]+")
            return urlRegex.find(text)?.value
        }
        return null
    }
}
