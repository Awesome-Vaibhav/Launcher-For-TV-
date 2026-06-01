package vaibhav.all.apps.launcher

import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for AppItem data class
 * These tests verify the data model integrity
 */
class AppItemTest {

    @Test
    fun `AppItem can be created with required fields`() {
        val app = AppItem(
            id = "test_app",
            name = "Test App",
            packageName = "com.test.app",
            isSystem = false
        )

        assertEquals("test_app", app.id)
        assertEquals("Test App", app.name)
        assertEquals("com.test.app", app.packageName)
        assertFalse(app.isSystem)
    }

    @Test
    fun `AppItem has correct default values`() {
        val app = AppItem(
            id = "test",
            name = "Test",
            packageName = "com.test",
            isSystem = false
        )

        assertEquals(Color.Gray, app.brandColor)
        assertEquals("", app.description)
        assertEquals("Streaming", app.category)
        assertEquals("4.5 ⭐", app.rating)
        assertEquals(listOf("UHD", "HDR", "Stereo"), app.specs)
        assertEquals("Featured Live Stream", app.mockShow)
        assertNull(app.launchIntent)
        assertNull(app.iconDrawable)
    }

    @Test
    fun `AppItem can be created with custom values`() {
        val app = AppItem(
            id = "custom_app",
            name = "Custom App",
            packageName = "com.custom.app",
            isSystem = true,
            brandColor = Color.Red,
            description = "Custom description",
            category = "Games",
            rating = "5.0 ⭐",
            specs = listOf("4K", "HDR10"),
            mockShow = "Custom Show"
        )

        assertEquals("custom_app", app.id)
        assertEquals("Custom App", app.name)
        assertEquals("com.custom.app", app.packageName)
        assertTrue(app.isSystem)
        assertEquals(Color.Red, app.brandColor)
        assertEquals("Custom description", app.description)
        assertEquals("Games", app.category)
        assertEquals("5.0 ⭐", app.rating)
        assertEquals(listOf("4K", "HDR10"), app.specs)
        assertEquals("Custom Show", app.mockShow)
    }

    @Test
    fun `AppItem equality works correctly`() {
        val app1 = AppItem(
            id = "test",
            name = "Test",
            packageName = "com.test",
            isSystem = false
        )

        val app2 = AppItem(
            id = "test",
            name = "Test",
            packageName = "com.test",
            isSystem = false
        )

        val app3 = AppItem(
            id = "different",
            name = "Different",
            packageName = "com.different",
            isSystem = false
        )

        assertEquals(app1, app2)
        assertNotEquals(app1, app3)
    }

    @Test
    fun `AppItem copy works correctly`() {
        val original = AppItem(
            id = "original",
            name = "Original",
            packageName = "com.original",
            isSystem = false,
            brandColor = Color.Blue
        )

        val copy = original.copy(name = "Modified")

        assertEquals("original", copy.id)
        assertEquals("Modified", copy.name)
        assertEquals("com.original", copy.packageName)
        assertEquals(Color.Blue, copy.brandColor)
    }
}
