```kotlin
package com.zenithguard.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.zenithguard.core.ProtectionEngine

/**
 * Zenith Guard - SMS & Phishing Dolandırıcılık Engelleyici
 * Gelen SMS mesajlarında sahte banka bağlantılarını ve oltama (phishing) linklerini süzer.
 */
class SmsReceiver : BroadcastReceiver() {

    private val suspiciousKeywords = listOf(
        "tebrikler kazandınız", "hesabınız askıya alındı", "banka doğrulama",
        "tıklayın", "pTT kargo", "icra takibi", "aidat iadesi", "giriş yapın"
    )

    private val suspiciousDomains = listOf(
        "bit.ly", "tinyurl.com", ".xyz", ".top", ".tk", ".site", "banka-guncelle"
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val protectionEngine = ProtectionEngine(context)
            if (!protectionEngine.isModuleEnabled(ProtectionEngine.MODULE_SMS_ANTI_PHISHING, true)) {
                return
            }

            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val messageBody = sms.messageBody ?: continue
                val sender = sms.originatingAddress ?: "Bilinmeyen Gönderici"

                val isPhishing = checkPhishingContent(messageBody)
                if (isPhishing) {
                    Log.e("ZenithGuard", "ŞÜPHELİ SMS ENGELLEDİ! Gönderici: $sender | İçerik: $messageBody")
                    // Zararlı SMS tespit uyarısı
                }
            }
        }
    }

    private fun checkPhishingContent(text: String): Boolean {
        val lowerText = text.lowercase()
        val hasKeyword = suspiciousKeywords.any { lowerText.contains(it) }
        val hasSuspiciousLink = suspiciousDomains.any { lowerText.contains(it) }

        return hasKeyword || hasSuspiciousLink
    }
}
```
