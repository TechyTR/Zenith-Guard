```cpp
#include <jni.h>
#include <string>
#include <unistd.h>
#include <sys/ptrace.h>

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_zenithguard_core_NativeSecurityEngine_nativeCheckDebugger(JNIEnv *env, jobject thiz) {
    // Attempt to attach debugger to self. If another debugger is already attached, ptrace will fail.
    if (ptrace(PTRACE_TRACEME, 0, 1, 0) < 0) {
        return JNI_TRUE; // Debugger detected!
    }
    return JNI_FALSE; // Safe
}

JNIEXPORT jboolean JNICALL
Java_com_zenithguard_core_NativeSecurityEngine_nativeCheckSuBinaries(JNIEnv *env, jobject thiz) {
    const char *suPaths[] = {
        "/system/bin/su",
        "/system/xbin/su",
        "/sbin/su",
        "/system/sd/xbin/su",
        "/data/local/su",
        "/data/local/bin/su",
        "/su/bin/su"
    };

    for (const char *path : suPaths) {
        if (access(path, F_OK) == 0) {
            return JNI_TRUE; // Root binary found natively
        }
    }
    return JNI_FALSE;
}

JNIEXPORT jstring JNICALL
Java_com_zenithguard_core_NativeSecurityEngine_nativeGetSecurityHash(JNIEnv *env, jobject thiz) {
    std::string secureHash = "ZENITH_GUARD_SECURE_NATIVE_HASH_998127";
    return env->NewStringUTF(secureHash.c_str());
}

}
```
