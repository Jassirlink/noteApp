package com.devquest.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.devquest.noteapp.presentation.navigation.AppNavigation
import com.devquest.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - The single Activity for the entire app.
 *
 * CONCEPT: @AndroidEntryPoint
 * ----------------------------
 * This annotation tells Hilt: "This Activity needs dependency injection."
 * Hilt generates code to inject dependencies into this Activity.
 *
 * In a Compose app we only ever need ONE Activity.
 * All screens are Composable functions — no more one-Activity-per-screen!
 *
 * CONCEPT: setContent { }
 * ------------------------
 * This is where we "attach" our Compose UI tree to the Activity window.
 * Everything inside setContent is Compose.
 *
 * CONCEPT: enableEdgeToEdge()
 * ----------------------------
 * Makes the app draw behind the status bar and navigation bar for a
 * modern, immersive look. Scaffold + WindowInsets handle the padding
 * so content isn't hidden behind system bars.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw the app edge-to-edge (behind status bar & nav bar)
        enableEdgeToEdge()

        setContent {
            // NoteAppTheme applies Material3 colors, typography, shapes
            NoteAppTheme {
                // Surface fills the screen with the background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // AppNavigation sets up the NavHost with all screens
                    // This is the entry point to our entire navigation graph
                    AppNavigation()
                }
            }
        }
    }
}
