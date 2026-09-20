package com.zenithguard.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zenithguard.core.ProtectionEngine
import com.zenithguard.core.ShizukuManager

class ScreenStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_SCREEN_OFF) {
            return
        }

        val protectionEngine = ProtectionEngine(context)
        val shizukuManager = ShizukuManager(context)

        if (!protectionEngine.isModuleEnabled(
                ProtectionEngine.MODULE_AUTO_FREEZER,
                false
            )
        ) {
            return
        }

        if (!shizukuManager.hasShizukuPermission()) {
            Log.d(
                "ZenithGuard",
                "Shizuku yetkisi olmadığından otomatik dondurma atlandı."
            )
            return
        }

        Log.d(
            "ZenithGuard",
            "Ekran kapandı. Koruma hedefleri kontrol ediliyor..."
        )

        val candidateTargetsToFreeze = listOf(
            "com.facebook.katana",
            "com.facebook.orca",
            "com.mipush.sdk"
        )

        for (packageName in candidateTargetsToFreeze) {

            if (isPackageRunningImportantTask(context, packageName)) {
                Log.i(
                    "ZenithGuard",
                    "Güvenlik atlaması: $packageName kritik işlem yürütüyor."
                )
                continue
            }

            val success = shizukuManager.freezePackage(packageName)

            if (success) {
                Log.d(
                    "ZenithGuard",
                    "Güvenli dondurma uygulandı: $packageName"
                )
            } else {
                Log.w(
                    "ZenithGuard",
                    "Dondurma başarısız: $packageName"
                )
            }
        }
    }

    private fun isPackageRunningImportantTask(
        context: Context,
        packageName: String
    ): Boolean {
        /*
         * İleride ActivityManager / UsageStats / foreground-service
         * kontrolleri burada uygulanabilir.
         *
         * Varsayılan olarak false bırakılıyor.
         */
        return false
    }
}
