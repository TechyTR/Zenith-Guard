package com.zenithguard.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.zenithguard.core.ProtectionEngine

/**
 * Zenith Guard - Canlı Kurulum Dinleyicisi
 * Cihaza yeni bir APK veya uygulama yüklendiğinde anında devreye girer.
 */
class PackageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_PACKAGE_ADDED || action == Intent.ACTION_PACKAGE_REPLACED) {
            val packageName = intent.data?.schemeSpecificPart ?: return
            
            val protectionEngine = ProtectionEngine(context)
            if (!protectionEngine.isModuleEnabled(ProtectionEngine.MODULE_REALTIME_INSTALL, true)) {
                return // Modül kapalıysa işlem yapma
            }

            Log.d("ZenithGuard", "Yeni uygulama tespit edildi: $packageName. Canlı analiz başlatılıyor...")
            
            // Yüklenen uygulamanın kritik izinlerini analiz et
            analyzePackagePermissions(context, packageName)
        }
    }

    private fun analyzePackagePermissions(context: Context, packageName: String) {
        try {
            val pm = context.packageManager
            val pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val requestedPermissions = pkgInfo.requestedPermissions ?: return

            val suspiciousPermissions = mutableListOf<String>()
            for (perm in requestedPermissions) {
                when (perm) {
                    android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.RECEIVE_SMS -> suspiciousPermissions.add("SMS Okuma/Alma")
                    android.Manifest.permission.READ_CONTACTS -> suspiciousPermissions.add("Rehber Erişimi")
                    android.Manifest.permission.RECORD_AUDIO -> suspiciousPermissions.add("Mikrofon Erişimi")
                    android.Manifest.permission.CAMERA -> suspiciousPermissions.add("Kamera Erişimi")
                    android.Manifest.permission.SYSTEM_ALERT_WINDOW -> suspiciousPermissions.add("Ekranda Üst Katman Oluşturma (Overlay)")
                }
            }

            if (suspiciousPermissions.isNotEmpty()) {
                Log.w("ZenithGuard", "TEHLİKE TESPİT EDİLDİ: $packageName şu hassas izinleri istiyor: $suspiciousPermissions")
                // İleriki adımda sisteme canlı güvenlik uyarısı (Notification) tetiklenir
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
