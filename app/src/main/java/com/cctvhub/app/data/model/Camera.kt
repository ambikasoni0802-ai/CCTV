package com.cctvhub.app.data.model

enum class CameraStatus { ONLINE, OFFLINE, CONNECTING }

data class Camera(
    val id: String,
    val name: String,
    val ipAddress: String,
    val port: Int = 554,
    val username: String = "",
    val password: String = "",
    val rtspPath: String = "/stream1",
    val status: CameraStatus = CameraStatus.OFFLINE
) {
    fun buildRtspUrl(): String {
        val creds = if (username.isNotEmpty()) "$username:$password@" else ""
        return "rtsp://$creds$ipAddress:$port$rtspPath"
    }
}
