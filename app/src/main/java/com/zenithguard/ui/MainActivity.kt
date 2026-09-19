```kotlin
package com.zenithguard.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenithguard.core.ProtectionEngine
import com.zenithguard.core.ProtectionModule
import com.zenithguard.core.ShizukuManager

class MainActivity : ComponentActivity() {

    private lateinit var shizukuManager: ShizukuManager
    private lateinit var protectionEngine: ProtectionEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shizukuManager = ShizukuManager(this)
        protectionEngine = ProtectionEngine(this)

        setContent {
            ZenithGuardTheme {
                DashboardScreen(shizukuManager, protectionEngine)
            }
        }
    }
}

@Composable
fun DashboardScreen(shizukuManager: ShizukuManager, protectionEngine: ProtectionEngine) {
    var isShizukuActive by remember { mutableStateOf(shizukuManager.hasShizukuPermission()) }
    var modules by remember { mutableStateOf(protectionEngine.getModules(isShizukuActive)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ZENITH",
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                color = Color.White
            )
            Text(
                text = "GUARD",
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                color = Color(0xFF38BDF8)
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = Color(0xFF38BDF8).copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Max Protection Core",
                    color = Color(0xFF38BDF8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Shizuku ADB Ayrıcalığı",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (isShizukuActive) "Aktif (Derin Güvenlik Hazır)" else "Pasif (Standart Koruma)",
                        color = if (isShizukuActive) Color(0xFF10B981) else Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                if (!isShizukuActive) {
                    Button(
                        onClick = {
                            shizukuManager.requestPermission(1001)
                            isShizukuActive = shizukuManager.hasShizukuPermission()
                            modules = protectionEngine.getModules(isShizukuActive)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8))
                    ) {
                        Text("Yetki Ver", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "GÜVENLİK VE KORUMA MODÜLLERİ",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(modules) { module ->
                ModuleToggleCard(
                    module = module,
                    onToggleChanged = { enabled ->
                        protectionEngine.setModuleEnabled(module.id, enabled)
                        modules = protectionEngine.getModules(isShizukuActive)
                    }
                )
            }
        }
    }
}

@Composable
fun ModuleToggleCard(module: ProtectionModule, onToggleChanged: (Boolean) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = module.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (module.requiresShizuku) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFA855F7).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "ADB/Shizuku",
                                color = Color(0xFFA855F7),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = module.description,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = module.isEnabled,
                onCheckedChange = { onToggleChanged(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = Color(0xFF38BDF8),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color(0xFF21262D)
                )
            )
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
