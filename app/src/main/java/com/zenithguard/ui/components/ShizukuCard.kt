```kotlin
package com.zenithguard.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zenithguard.core.ShizukuManager
import com.zenithguard.ui.theme.AccentCyan
import com.zenithguard.ui.theme.AccentPurple
import com.zenithguard.ui.theme.CardSurface
import com.zenithguard.ui.theme.StatusGreen
import com.zenithguard.ui.theme.StatusWarning

@Composable
fun ShizukuCard(
    isShizukuActive: Boolean,
    shizukuManager: ShizukuManager,
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
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
                    .background(if (isShizukuActive) StatusGreen else StatusWarning)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Shizuku ADB Süper Ayrıcalığı",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Text(
                    text = if (isShizukuActive) "Aktif (Derin Kilit ve AppOps Hazır)" else "Pasif (Standart Mod)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            if (!isShizukuActive) {
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Yetki Ver", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = {
                        shizukuManager.forceStopApp("com.facebook.katana")
                        Toast.makeText(context, "Derin ADB Temizliği Yapıldı!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Derin Temizle", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
```
