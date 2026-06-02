package vaibhav.all.apps.launcher

import android.util.Log

object NativeStringMatcher {
    private var isNativeLibraryLoaded = false

    init {
        try {
            System.loadLibrary("native-utils")
            isNativeLibraryLoaded = true
        } catch (e: UnsatisfiedLinkError) {
            Log.e("NativeStringMatcher", "Native library native-utils not found, falling back to Kotlin implementation", e)
        }
    }

    private external fun containsIgnoreCaseNative(text: String, query: String): Boolean

    fun containsIgnoreCase(text: String, query: String): Boolean {
        if (query.isBlank()) return true
        
        return if (isNativeLibraryLoaded) {
            try {
                containsIgnoreCaseNative(text, query)
            } catch (e: Throwable) {
                Log.e("NativeStringMatcher", "Error calling native method, falling back to Kotlin", e)
                text.contains(query, ignoreCase = true)
            }
        } else {
            text.contains(query, ignoreCase = true)
        }
    }
}
