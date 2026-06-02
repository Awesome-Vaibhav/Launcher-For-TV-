package com.tvapp.launcher

import android.content.pm.ApplicationInfo
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for AppCategoryClassifier
 * These tests verify that apps are correctly categorized based on package names and labels
 */
class AppCategoryClassifierTest {

    @Test
    fun `getCategory returns Games for game packages`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Games", AppCategoryClassifier.getCategory("com.example.game", "My Game", appInfo))
        assertEquals("Games", AppCategoryClassifier.getCategory("com.arcade.shooter", "Shooter", appInfo))
        assertEquals("Games", AppCategoryClassifier.getCategory("com.playgames.android", "Play Games", appInfo))
        assertEquals("Games", AppCategoryClassifier.getCategory("com.retroarch.app", "RetroArch", appInfo))
    }

    @Test
    fun `getCategory returns Games for game labels`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Games", AppCategoryClassifier.getCategory("com.example.app", "Puzzle Game", appInfo))
        assertEquals("Games", AppCategoryClassifier.getCategory("com.example.app", "Arcade Fun", appInfo))
    }

    @Test
    fun `getCategory returns Productivity for office apps`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Productivity", AppCategoryClassifier.getCategory("com.microsoft.office.word", "Word", appInfo))
        assertEquals("Productivity", AppCategoryClassifier.getCategory("com.google.android.apps.docs.editors.sheets", "Sheets", appInfo))
        assertEquals("Productivity", AppCategoryClassifier.getCategory("com.example.calendar", "Calendar", appInfo))
        assertEquals("Productivity", AppCategoryClassifier.getCategory("com.adobe.reader", "PDF Reader", appInfo))
    }

    @Test
    fun `getCategory returns Social for messaging apps`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Social", AppCategoryClassifier.getCategory("com.whatsapp", "WhatsApp", appInfo))
        assertEquals("Social", AppCategoryClassifier.getCategory("com.facebook.katana", "Facebook", appInfo))
        assertEquals("Social", AppCategoryClassifier.getCategory("com.instagram.android", "Instagram", appInfo))
        assertEquals("Social", AppCategoryClassifier.getCategory("com.discord", "Discord", appInfo))
        assertEquals("Social", AppCategoryClassifier.getCategory("com.example.messenger", "Messenger", appInfo))
    }

    @Test
    fun `getCategory returns Entertainment for streaming apps`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Entertainment", AppCategoryClassifier.getCategory("com.netflix.mediaclient", "Netflix", appInfo))
        assertEquals("Entertainment", AppCategoryClassifier.getCategory("com.google.android.youtube.tv", "YouTube", appInfo))
        assertEquals("Entertainment", AppCategoryClassifier.getCategory("com.spotify.tv.android", "Spotify", appInfo))
        assertEquals("Entertainment", AppCategoryClassifier.getCategory("com.disney.disneyplus", "Disney+", appInfo))
        assertEquals("Entertainment", AppCategoryClassifier.getCategory("tv.twitch.android.app", "Twitch", appInfo))
    }

    @Test
    fun `getCategory returns Utilities for system apps`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Utilities", AppCategoryClassifier.getCategory("com.android.settings", "Settings", appInfo))
        assertEquals("Utilities", AppCategoryClassifier.getCategory("com.example.calculator", "Calculator", appInfo))
        assertEquals("Utilities", AppCategoryClassifier.getCategory("com.weather.app", "Weather", appInfo))
        assertEquals("Utilities", AppCategoryClassifier.getCategory("com.files.manager", "File Manager", appInfo))
    }

    @Test
    fun `getCategory returns Other for unrecognized apps`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Other", AppCategoryClassifier.getCategory("com.random.unknown", "Unknown App", appInfo))
        assertEquals("Other", AppCategoryClassifier.getCategory("com.xyz.abc", "XYZ", appInfo))
    }

    @Test
    fun `getCategory handles null appInfo gracefully`() {
        // Should not crash with null appInfo
        val result = AppCategoryClassifier.getCategory("com.example.app", "Test App", null)
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `getCategory is case insensitive`() {
        val appInfo = ApplicationInfo()
        
        assertEquals("Games", AppCategoryClassifier.getCategory("com.EXAMPLE.GAME", "MY GAME", appInfo))
        assertEquals("Social", AppCategoryClassifier.getCategory("com.WHATSAPP", "WHATSAPP", appInfo))
        assertEquals("Entertainment", AppCategoryClassifier.getCategory("com.NETFLIX.MEDIACLIENT", "NETFLIX", appInfo))
    }
}
