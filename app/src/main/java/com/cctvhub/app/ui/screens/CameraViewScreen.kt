package com.cctvhub.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // <--- Yeh missing import add karein
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CameraViewScreen() {
    Scaffold { paddingValues ->
        // Direct 'padding' likhne ke bajaye 'Modifier.padding()' use karein
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Scaffold ka padding yahan apply hota hai
        ) {
            Text(text = "Camera View Screen")
        }
    }
}
