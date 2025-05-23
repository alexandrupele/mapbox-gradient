package com.pelworks.mapboxgradient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pelworks.mapboxgradient.presentation.MainScreen
import com.pelworks.mapboxgradient.presentation.MainViewModel
import com.pelworks.mapboxgradient.presentation.theme.MapboxGradientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapboxGradientTheme {
                Scaffold { contentPadding ->
                    val viewModel: MainViewModel = viewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    MainScreen(
                        state = state,
                        modifier = Modifier.padding(contentPadding)
                    )
                }
            }
        }
    }
}