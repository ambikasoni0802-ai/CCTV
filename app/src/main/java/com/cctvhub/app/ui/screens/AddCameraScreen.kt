package com.cctvhub.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cctvhub.app.data.model.Camera
import com.cctvhub.app.onvif.DiscoveredDevice
import com.cctvhub.app.onvif.OnvifDiscovery
import com.cctvhub.app.ui.theme.*
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCameraScreen(onSave: (Camera) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("554") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var scanning by remember { mutableStateOf(false) }
    var scanDone by remember { mutableStateOf(false) }
    var foundDevices by remember { mutableStateOf<List<DiscoveredDevice>>(emptyList()) }

    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = { Text("Add Camera") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDeep, titleContentColor = TextPrimary)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Button(
                    onClick = {
                        scanning = true
                        scanDone = false
                        scope.launch {
                            foundDevices = OnvifDiscovery.discoverDevices(context)
                            scanning = false
                            scanDone = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (scanning) "Scanning network..." else "Scan Network for Cameras", color = BgDeep)
                }
            }

            if (scanDone && foundDevices.isEmpty()) {
                item {
                    Text(
                        "No ONVIF cameras found on this WiFi. You can still enter the IP address manually below.",
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            items(foundDevices) { device ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(device.ipAddress, color = TextPrimary)
                        TextButton(onClick = { ip = device.ipAddress }) {
                            Text("Use this", color = AccentCyan)
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Camera Name") }, modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = ip, onValueChange = { ip = it },
                    label = { Text("IP Address") }, modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = port, onValueChange = { port = it },
                    label = { Text("RTSP Port (default 554)") }, modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = username, onValueChange = { username = it },
                    label = { Text("Username") }, modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Password") }, modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val camera = Camera(
                                id = UUID.randomUUID().toString(),
                                name = name.ifBlank { "Camera" },
                                ipAddress = ip,
                                port = port.toIntOrNull() ?: 554,
                                username = username,
                                password = password
                            )
                            onSave(camera)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Camera", color = BgDeep)
                    }
                }
            }
        }
    }
}
