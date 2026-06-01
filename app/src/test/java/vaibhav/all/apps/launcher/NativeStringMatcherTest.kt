package vaibhav.all.apps.launcher

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for string matching logic
 * These tests verify case-insensitive search functionality
 */
class StringMatchingTest {

    @Test
    fun `case insensitive search returns true for matching strings`() {
        assertTrue("YouTube TV".contains("youtube", ignoreCase = true))
        assertTrue("Netflix".contains("net", ignoreCase = true))
        assertTrue("Prime Video".contains("PRIME", ignoreCase = true))
        assertTrue("Disney+".contains("disney", ignoreCase = true))
    }

    @Test
    fun `case insensitive search returns false for non-matching strings`() {
        assertFalse("YouTube TV".contains("netflix", ignoreCase = true))
        assertFalse("Prime Video".contains("hulu", ignoreCase = true))
        assertFalse("Spotify".contains("apple", ignoreCase = true))
    }

    @Test
    fun `empty query matches all strings`() {
        assertTrue("Netflix".contains("", ignoreCase = true))
        assertTrue("YouTube TV".contains("", ignoreCase = true))
        assertTrue("Prime Video".contains("", ignoreCase = true))
    }

    @Test
    fun `search handles special characters`() {
        assertTrue("Disney+".contains("+", ignoreCase = true))
        assertTrue("HBO Max".contains(" ", ignoreCase = true))
    }

    @Test
    fun `search handles partial matches`() {
        assertTrue("YouTube TV".contains("you", ignoreCase = true))
        assertTrue("YouTube TV".contains("tube", ignoreCase = true))
        assertTrue("YouTube TV".contains("tv", ignoreCase = true))
    }

    @Test
    fun `search is truly case insensitive`() {
        assertTrue("Netflix".contains("NETFLIX", ignoreCase = true))
        assertTrue("Netflix".contains("netflix", ignoreCase = true))
        assertTrue("Netflix".contains("NeTfLiX", ignoreCase = true))
    }

    @Test
    fun `package name search works correctly`() {
        val packageName = "com.netflix.mediaclient"
        
        assertTrue(packageName.contains("netflix", ignoreCase = true))
        assertTrue(packageName.contains("media", ignoreCase = true))
        assertFalse(packageName.contains("youtube", ignoreCase = true))
    }
}
