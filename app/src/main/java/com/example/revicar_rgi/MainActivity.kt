package com.example.revicar_rgi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.revicar_rgi.ui.screens.HomeScreen
import com.example.revicar_rgi.ui.theme.Revicar_RGITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Revicar_RGITheme {
                HomeScreen()
            }
        }
    }
}