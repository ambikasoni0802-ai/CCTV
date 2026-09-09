package com.cctvhub.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cctvhub.app.data.model.Camera
import com.cctvhub.app.data.model.CameraStatus
import com.cctvhub.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(cameras: List<Camera>) {
    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = { Text("CCTV Hub", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDeep, titleContentColor = TextPrimary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }, containerColor = AccentCyan) {
                Icon(Icons.Default.Add, contentDescription = "Add Camera", tint = BgDeep)
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            items(cameras) { camera ->
                CameraCard(camera)
            }
        }
    }
}

@Composable
fun CameraCard(camera: Camera) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        modifier = Modifier.aspectRatio(1f)
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier.fillMaxSize().background(BgSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Videocam, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(40.dp))
            }
            val dotColor = if (camera.status == CameraStatus.ONLINE) StatusOnline else StatusOffline
            Box(
                Modifier.align(Alignment.TopEnd).padding(8.dp).clip(RoundedCornerShape(50)).background(dotColor).size(10.dp)
            )
            Text(
                camera.name,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
            )
        }
    }
}
