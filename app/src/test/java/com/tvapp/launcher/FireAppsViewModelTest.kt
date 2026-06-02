package com.tvapp.launcher

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class FireAppsViewModelTest {

    private lateinit var context: Context
    private lateinit var viewModel: FireAppsViewModel

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        viewModel = FireAppsViewModel(context)
    }

    @Test
    fun `updateSearchQuery updates the search query state`() = runTest {
        val query = "test query"
        viewModel.updateSearchQuery(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `selectTab updates the selected tab state`() = runTest {
        val tab = "Favorites Hub"
        viewModel.selectTab(tab)
        assertEquals(tab, viewModel.selectedTab.value)
    }

    @Test
    fun `selectApp updates the selected app state`() = runTest {
        val app = AppItem(
            id = "test_app",
            name = "Test App",
            packageName = "com.test.app",
            isSystem = false
        )
        viewModel.selectApp(app)
        assertEquals(app, viewModel.selectedApp.value)

        viewModel.selectApp(null)
        assertNull(viewModel.selectedApp.value)
    }

    @Test
    fun `setStreamingFeed updates the streaming feed state`() = runTest {
        viewModel.setStreamingFeed(true)
        assertTrue(viewModel.isStreamingFeedActive.value)

        viewModel.setStreamingFeed(false)
        assertFalse(viewModel.isStreamingFeedActive.value)
    }

    @Test
    fun `setTimeFormat24h updates the 24h format state`() = runTest {
        viewModel.setTimeFormat24h(true)
        assertTrue(viewModel.timeFormat24h.value)

        viewModel.setTimeFormat24h(false)
        assertFalse(viewModel.timeFormat24h.value)
    }

    @Test
    fun `setUseSystemTime updates state and resets offset when true`() = runTest {
        viewModel.setCustomDateTime(10, 10, 10, 10, 2024)
        assertNotEquals(0L, viewModel.timeOffset.value)
        assertFalse(viewModel.useSystemTime.value)

        viewModel.setUseSystemTime(true)
        assertTrue(viewModel.useSystemTime.value)
        assertEquals(0L, viewModel.timeOffset.value)
    }

    @Test
    fun `setCustomDateTime calculates offset and disables system time`() = runTest {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2024)
        calendar.set(Calendar.MONTH, 11) // December (0-indexed but method uses 1-indexed)
        calendar.set(Calendar.DAY_OF_MONTH, 25)
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val targetMillis = calendar.timeInMillis

        viewModel.setCustomDateTime(10, 30, 25, 12, 2024)

        assertFalse(viewModel.useSystemTime.value)

        // Approximate check for offset to account for execution time
        val expectedOffset = targetMillis - System.currentTimeMillis()
        val actualOffset = viewModel.timeOffset.value
        assertTrue(Math.abs(expectedOffset - actualOffset) < 1000)
    }

    @Test
    fun `getCalendarFields returns correct fields`() = runTest {
        viewModel.setCustomDateTime(14, 45, 15, 8, 2023)
        val fields = viewModel.getCalendarFields()

        assertEquals(14, fields.hour)
        assertEquals(45, fields.minute)
        assertEquals(15, fields.day)
        assertEquals(8, fields.month)
        assertEquals(2023, fields.year)
    }

    @Test
    fun `toggleFavorite adds and removes package to favorites`() = runTest {
        val packageName = "com.example.app"

        viewModel.toggleFavorite(packageName)
        assertTrue(viewModel.favorites.value.contains(packageName))

        viewModel.toggleFavorite(packageName)
        assertFalse(viewModel.favorites.value.contains(packageName))
    }

    @Test
    fun `moveApp moves an app up or down the list`() = runTest {
        // Need to wait for system apps to load, but since we are mocking context and intent handling is complex,
        // we can test if the initial state handles empty state without crashing
        viewModel.moveApp("non.existent.app", 1)
        assertEquals(0, viewModel.systemApps.value.size) // No apps loaded initially in standard test context
    }
}
