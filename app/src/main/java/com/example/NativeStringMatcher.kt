package com.example

import java.util.Locale

object NativeStringMatcher {
    private val nativeLoaded: Boolean = runCatching {
        System.loadLibrary("native-utils")
        true
    }.getOrDefault(false)

    private external fun containsIgnoreCaseNative(text: String, query: String): Boolean

    fun containsIgnoreCase(text: String, query: String): Boolean {
        if (query.isBlank()) return true

        if (nativeLoaded) {
            runCatching {
                return containsIgnoreCaseNative(text, query)
            }
        }

        return text.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
    }
}
