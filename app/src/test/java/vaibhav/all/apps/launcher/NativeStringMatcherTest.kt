package vaibhav.all.apps.launcher

import android.os.Build
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field

/**
 * Unit tests for string matching logic
 * These tests verify case-insensitive search functionality and the fallback logic
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class NativeStringMatcherTest {

    private lateinit var isNativeLibraryLoadedField: Field
    private var originalIsNativeLibraryLoaded: Boolean = false

    @Before
    fun setUp() {
        isNativeLibraryLoadedField = NativeStringMatcher::class.java.getDeclaredField("isNativeLibraryLoaded")
        isNativeLibraryLoadedField.isAccessible = true
        originalIsNativeLibraryLoaded = isNativeLibraryLoadedField.getBoolean(NativeStringMatcher)
    }

    @After
    fun tearDown() {
        isNativeLibraryLoadedField.setBoolean(NativeStringMatcher, originalIsNativeLibraryLoaded)
    }

    @Test
    fun `case insensitive search returns true for matching strings`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("YouTube TV", "youtube"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("Netflix", "net"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("Prime Video", "PRIME"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("Disney+", "disney"))
    }

    @Test
    fun `case insensitive search returns false for non-matching strings`() {
        assertFalse(NativeStringMatcher.containsIgnoreCase("YouTube TV", "netflix"))
        assertFalse(NativeStringMatcher.containsIgnoreCase("Prime Video", "hulu"))
        assertFalse(NativeStringMatcher.containsIgnoreCase("Spotify", "apple"))
    }

    @Test
    fun `empty query matches all strings`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("Netflix", ""))
        assertTrue(NativeStringMatcher.containsIgnoreCase("YouTube TV", ""))
        assertTrue(NativeStringMatcher.containsIgnoreCase("Prime Video", ""))
    }

    @Test
    fun `search handles special characters`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("Disney+", "+"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("HBO Max", " "))
    }

    @Test
    fun `search handles partial matches`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("YouTube TV", "you"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("YouTube TV", "tube"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("YouTube TV", "tv"))
    }

    @Test
    fun `search is truly case insensitive`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("Netflix", "NETFLIX"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("Netflix", "netflix"))
        assertTrue(NativeStringMatcher.containsIgnoreCase("Netflix", "NeTfLiX"))
    }

    @Test
    fun `package name search works correctly`() {
        val packageName = "com.netflix.mediaclient"
        
        assertTrue(NativeStringMatcher.containsIgnoreCase(packageName, "netflix"))
        assertTrue(NativeStringMatcher.containsIgnoreCase(packageName, "media"))
        assertFalse(NativeStringMatcher.containsIgnoreCase(packageName, "youtube"))
    }

    @Test
    fun `fallback to Kotlin works when native method throws error - match`() {
        // Force the native library to be considered "loaded"
        isNativeLibraryLoadedField.setBoolean(NativeStringMatcher, true)

        // Native method isn't actually loaded, so it will throw UnsatisfiedLinkError
        // We expect it to catch it and use the Kotlin fallback
        val result = NativeStringMatcher.containsIgnoreCase("Netflix", "net")
        assertTrue("Fallback matching should return true", result)
    }

    @Test
    fun `fallback to Kotlin works when native method throws error - no match`() {
        isNativeLibraryLoadedField.setBoolean(NativeStringMatcher, true)

        val result = NativeStringMatcher.containsIgnoreCase("Netflix", "xyz")
        assertFalse("Fallback matching should return false", result)
    }

    @Test
    fun `pure Kotlin path works when native library is not loaded - match`() {
        // Force the native library to be considered "not loaded"
        isNativeLibraryLoadedField.setBoolean(NativeStringMatcher, false)

        val result = NativeStringMatcher.containsIgnoreCase("Netflix", "net")
        assertTrue("Kotlin path matching should return true", result)
    }

    @Test
    fun `pure Kotlin path works when native library is not loaded - no match`() {
        isNativeLibraryLoadedField.setBoolean(NativeStringMatcher, false)

        val result = NativeStringMatcher.containsIgnoreCase("Netflix", "xyz")
        assertFalse("Kotlin path matching should return false", result)
    }
}
