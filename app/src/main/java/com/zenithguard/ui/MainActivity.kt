```kotlin
package com.zenithguard.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    // OriginOS / HarmonyOS / OneUI Tarzı Akıcı Canlı Renk Geçiş Animasyonu
    val infiniteTransition = rememberInfiniteTransition(label = "os_gradient")
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF0F172A),
        targetValue = Color(0xFF0284C7),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF311B92),
        targetValue = Color(0xFF0D9488),
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color2"
    )
    val color3 by infiniteTransition.animateColor(
        initialValue = Color(0xFF020617),
        targetValue = Color(0xFF1E1B4B),
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color3"
    )

    val animatedGradient = Brush.verticalGradient(
        colors = listOf(color1, color2, color3)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F17))
            .padding(16.dp)
    ) {
        // --- OriginOS / HarmonyOS / OneUI Tarzı Premium Sürüm Kartı ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(animatedGradient)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .shadow(16.dp, RoundedCornerShape(28.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Üst Durum Rozeti
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "SİSTEM GÜVENLİ VE GÜNCEL",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Zenith Version Etiketi
                Text(
                    text = "Zenith Version",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )

                // Devasa Sürüm Numarası (OneUI / HarmonyOS Style)
                Text(
                    text = "1",
                    color = Color.White,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 84.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Zenith Guard Lite Core • Build #1",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Shizuku ADB Durum Kartı
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (isShizukuActive) Color(0xFF10B981) else Color(0xFFF59E0B))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Shizuku ADB Ayrıcalığı",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (isShizukuActive) "Aktif (Derin Güvenlik Hazır)" else "Pasif (Standart Koruma)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                if (!isShizukuActive) {
                    Button(
                        onClick = {
                            shizukuManager.requestPermission(1001)
                            isShizukuActive = shizukuManager.hasShizukuPermission()
                            modules = protectionEngine.getModules(isShizukuActive)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Yetki Ver", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "GÜVENLİK VE KORUMA MODÜLLERİ",
            color = Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
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
        shape = RoundedCornerShape(14.dp),
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
                                text = "ADB",
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
            background = Color(0xFF0B0F17),
            surface = Color(0xFF161B22),
            primary = Color(0xFF38BDF8)
        ),
        content = content
    )
}
```
