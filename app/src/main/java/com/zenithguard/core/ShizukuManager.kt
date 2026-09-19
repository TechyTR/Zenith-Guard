```kotlin
// DOSYA KONUMU: app/src/main/java/com/zenithguard/core/ShizukuManager.kt

package com.zenithguard.core

import android.content.Context
import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

class ShizukuManager(private val context: Context) {

    interface ShizukuStateCallback {
        fun onConnected()
        fun onDisconnected()
        fun onPermissionDenied()
    }

    private var callback: ShizukuStateCallback? = null

    fun registerListener(callback: ShizukuStateCallback) {
        this.callback = callback
        Shizuku.addBinderReceivedListener { callback.onConnected() }
        Shizuku.addBinderDeadListener { callback.onDisconnected() }
    }

    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    fun hasShizukuPermission(): Boolean {
        if (!isShizukuAvailable()) return false
        return if (Shizuku.isPreV11()) {
            false
        } else {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermission(requestCode: Int) {
        if (isShizukuAvailable()) {
            Shizuku.requestPermission(requestCode)
        }
    }

    fun freezePackage(packageName: String): Boolean {
        if (!hasShizukuPermission()) return false
        val command = "pm disable-user --user 0 $packageName"
        val result = executeAdbCommand(command)
        return result.contains("disabled-user") || result.contains("new state: disabled-user")
    }

    fun unfreezePackage(packageName: String): Boolean {
        if (!hasShizukuPermission()) return false
        val command = "pm enable $packageName"
        val result = executeAdbCommand(command)
        return result.contains("enabled") || result.contains("new state: enabled")
    }

    fun revokePermission(packageName: String, permission: String): Boolean {
        if (!hasShizukuPermission()) return false
        val command = "pm revoke $packageName $permission"
        val result = executeAdbCommand(command)
        return !result.contains("Error") && !result.contains("Exception")
    }

    fun forceStopApp(packageName: String): Boolean {
        if (!hasShizukuPermission()) return false
        val command = "am force-stop $packageName"
        executeAdbCommand(command)
        return true
    }

    fun setAppOpRestriction(packageName: String, opName: String, allow: Boolean): Boolean {
        if (!hasShizukuPermission()) return false
        val mode = if (allow) "allow" else "ignore"
        val command = "appops set $packageName $opName $mode"
        val result = executeAdbCommand(command)
        return !result.contains("Error")
    }

    fun setAppInactive(packageName: String, inactive: Boolean): Boolean {
        if (!hasShizukuPermission()) return false
        val state = if (inactive) "true" else "false"
        val command = "am set-inactive $packageName $state"
        executeAdbCommand(command)
        return true
    }

    private fun executeAdbCommand(command: String): String {
        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            process.waitFor()
            output.toString().trim()
        } catch (e: Exception) {
            e.printStackTrace()
            "ERROR: ${e.localizedMessage}"
        }
    }
}
```
