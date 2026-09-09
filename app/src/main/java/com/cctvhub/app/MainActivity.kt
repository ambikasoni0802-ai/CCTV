package com.cctvhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.cctvhub.app.data.model.Camera
import com.cctvhub.app.data.repository.CameraRepository
import com.cctvhub.app.ui.screens.AddCameraScreen
import com.cctvhub.app.ui.screens.CameraViewScreen
import com.cctvhub.app.ui.screens.DashboardScreen
import com.cctvhub.app.ui.theme.CCTVHubTheme

sealed class Screen {
    object Dashboard : Screen()
    object AddCamera : Screen()
    data class CameraView(val camera: Camera) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CCTVHubTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
                var cameras by remember { mutableStateOf(CameraRepository.loadCameras(this)) }

                when (val screen = currentScreen) {
                    is Screen.Dashboard -> DashboardScreen(
                        cameras = cameras,
                        onCameraClick = { currentScreen = Screen.CameraView(it) },
                        onAddClick = { currentScreen = Screen.AddCamera }
                    )
                    is Screen.AddCamera -> AddCameraScreen(
                        onSave = { camera ->
                            CameraRepository.saveCamera(this, camera)
                            cameras = CameraRepository.loadCameras(this)
                            currentScreen = Screen.Dashboard
                        },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                    is Screen.CameraView -> CameraViewScreen(
                        cameraName = screen.camera.name,
                        rtspUrl = screen.camera.buildRtspUrl(),
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }
            }
        }
    }
}
