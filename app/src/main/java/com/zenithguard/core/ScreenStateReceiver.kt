```kotlin
package com.zenithguard.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zenithguard.core.ProtectionEngine
import com.zenithguard.core.ShizukuManager

/**
 * Zenith Guard - Akıllı Ekran Kapanış Dondurucusu
 * Ekran kapandığı an arka planda çalışan gereksiz bloatware servislerini Shizuku ile askıya alır.
 */
class ScreenStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            val protectionEngine = ProtectionEngine(context)
            val shizukuManager = ShizukuManager(context)

            if (!protectionEngine.isModuleEnabled(ProtectionEngine.MODULE_AUTO_FREEZER, false)) {
                return
            }

            if (!shizukuManager.hasShizukuPermission()) {
                Log.d("ZenithGuard", "Shizuku yetkisi olmadığından otomatik dondurma atlandı.")
                return
            }

            Log.d("ZenithGuard", "Ekran kapandı! Bütçe dostu RAM/Pil koruma modu devreye giriyor...")
            
            // Arka planda RAM tüketen bilinen arka plan telemetry / bloatware servislerini dondur
            val targetsToFreeze = listOf(
                "com.facebook.katana",
                "com.facebook.orca",
                "com.mipush.sdk"
            )

            for (pkg in targetsToFreeze) {
                shizukuManager.freezePackage(pkg)
            }
        }
    }
}
```
