package com.cctvhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cctvhub.app.data.model.Camera
import com.cctvhub.app.data.model.CameraStatus
import com.cctvhub.app.ui.screens.DashboardScreen
import com.cctvhub.app.ui.theme.CCTVHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CCTVHubTheme {
                val demoCameras = listOf(
                    Camera(id = "1", name = "Main Gate", ipAddress = "192.168.1.10", status = CameraStatus.ONLINE),
                    Camera(id = "2", name = "Parking", ipAddress = "192.168.1.11", status = CameraStatus.OFFLINE),
                    Camera(id = "3", name = "Lobby", ipAddress = "192.168.1.12", status = CameraStatus.ONLINE),
                    Camera(id = "4", name = "Terrace", ipAddress = "192.168.1.13", status = CameraStatus.OFFLINE)
                )
                DashboardScreen(cameras = demoCameras)
            }
        }
    }
}
