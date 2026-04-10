package com.marcbajwa.laptimernative

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.marcbajwa.laptimernative.ui.LapTimerNativeApp
import com.marcbajwa.laptimernative.ui.theme.LapTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LapTimerTheme {
                LapTimerNativeApp()
            }
        }
    }
}
