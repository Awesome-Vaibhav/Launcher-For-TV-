package vaibhav.all.apps.launcher

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeStringMatcherTest {

    @Test
    fun `containsIgnoreCase returns true for case-insensitive app name`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("YouTube TV", "youtube"))
    }

    @Test
    fun `containsIgnoreCase returns true for blank query`() {
        assertTrue(NativeStringMatcher.containsIgnoreCase("Netflix", ""))
    }

    @Test
    fun `containsIgnoreCase returns false when value is missing`() {
        assertFalse(NativeStringMatcher.containsIgnoreCase("Prime Video", "hulu"))
    }
}
