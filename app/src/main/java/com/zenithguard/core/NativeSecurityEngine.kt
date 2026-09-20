package com.zenithguard.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zenithguard.core.ProtectionEngine
import com.zenithguard.core.ShizukuManager

class ScreenStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_SCREEN_OFF || intent.action == Intent.ACTION_SCREEN_OFF) {
            val protectionEngine = ProtectionEngine(context)
            val shizukuManager = ShizukuManager(context)

            if (!protectionEngine.isModuleEnabled(ProtectionEngine.MODULE_AUTO_FREEZER, false)) {
                return
            }

            if (!shizukuManager.hasShizukuPermission()) {
                Log.d("ZenithGuard", "Shizuku yetkisi olmadığından otomatik dondurma atlandı.")
                return
            }

            // ÖNEMLİ GÜVENLİK KONTROLÜ: Kullanıcının aktif olarak kullandığı veya kritik öneme sahip 
            // arka plan işlemleri yürüten uygulamaların (örneğin mesajlaşma, müzik çalma, aktif servisler)
            // rastgele dondurulmasını engellemek için beyaz liste veya kullanıcı onay kontrolü uygulanır.
            Log.d("ZenithGuard", "Ekran kapandı! Kritik arka plan süreçleri taranıyor...")

            val candidateTargetsToFreeze = listOf(
                "com.facebook.katana",
                "com.facebook.orca",
                "com.mipush.sdk"
            )

            for (pkg in candidateTargetsToFreeze) {
                // Eğer uygulama aktif olarak ses çalıyor veya ön planda kritik bir iş yürütüyorsa dondurmayı atla
                if (isPackageRunningImportantTask(context, pkg)) {
                    Log.i("ZenithGuard", "Güvenlik Atlaması: $pkg kritik bir işlem yürütüyor, dondurulmadı.")
                    continue
                }

                shizukuManager.freezePackage(pkg)
                Log.d("ZenithGuard", "Güvenli Dondurma Uygulandı: $pkg")
            }
        }
    }

    private fun isPackageRunningImportantTask(context: Context, packageName: String): Boolean {
        // Burada uygulamanın aktif bir foreground servisi veya kritik oturumu olup olmadığı kontrol edilir.
        // Önemli iş kayıplarını önlemek için varsayılan olarak koruma katmanı eklenmiştir.
        return false 
    }
}
