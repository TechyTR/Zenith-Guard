package com.zenithguard.shizuku

import android.os.RemoteException
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class ZenithUserService : IZenithUserService.Stub() {

    companion object {
        private const val TAG = "ZenithUserService"
    }

    override fun execute(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(
                arrayOf(
                    "sh",
                    "-c",
                    command
                )
            )

            val reader = BufferedReader(
                InputStreamReader(process.inputStream)
            )

            val output = StringBuilder()

            var line: String?

            while (
                reader.readLine().also {
                    line = it
                } != null
            ) {
                output.append(line)
                output.append('\n')
            }

            process.waitFor()

            output.toString().trim()

        } catch (e: Exception) {
            Log.e(
                TAG,
                "Command execution failed",
                e
            )

            "ERROR: ${e.localizedMessage}"
        }
    }

    override fun destroy() {
        Log.i(
            TAG,
            "Zenith UserService shutting down"
        )

        System.exit(0)
    }
}
