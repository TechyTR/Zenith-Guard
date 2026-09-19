```kotlin
package com.zenithguard.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenithguard.core.ShizukuManager

class MainActivity : ComponentActivity() {

    private lateinit var shizukuManager: ShizukuManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shizukuManager = ShizukuManager(this)

        setContent {
            ZenithGuardTheme {
                DashboardScreen(shizukuManager)
            }
        }
    }
}

@Composable
fun ZenithGuardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF0D1117),
            surface = Color(0xFF161B22),
            primary = Color(0xFF38BDF8)
        ),
        content = content
    )
}

@Composable
fun DashboardScreen(shizukuManager: ShizukuManager) {
    var isShizukuActive by remember { mutableStateOf(shizukuManager.hasShizukuPermission()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(20.dp)
    ) {
        // App Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ZENITH",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = Color.White
            )
            Text(
                text = "GUARD",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = Color(0xFF38BDF8)
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = Color(0xFF38BDF8).copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Lite Core v1.0",
                    color = Color(0xFF38BDF8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Status Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Shizuku ADB Durumu",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isShizukuActive) "Aktif - Tam Yetkili" else "Pasif - Bağlantı Bekleniyor",
                    color = if (isShizukuActive) Color(0xFF10B981) else Color(0xFFF59E0B),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {
                        shizukuManager.requestPermission(1001)
                        isShizukuActive = shizukuManager.hasShizukuPermission()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8))
                ) {
                    Text(
                        text = if (isShizukuActive) "Sistem Uygulamalarını Dondur" else "Shizuku İzni İste",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
```
