package vaibhav.all.apps.launcher

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for CuratedApps list
 * These tests verify the integrity of the curated apps data
 */
class CuratedAppsTest {

    @Test
    fun `CuratedApps list is not empty`() {
        assertTrue("CuratedApps should contain apps", CuratedApps.isNotEmpty())
    }

    @Test
    fun `CuratedApps contains expected streaming services`() {
        val packageNames = CuratedApps.map { it.packageName }
        
        assertTrue("Should contain Netflix", packageNames.contains("com.netflix.mediaclient"))
        assertTrue("Should contain Prime Video", packageNames.contains("com.amazon.amazonvideo.livingroom"))
        assertTrue("Should contain Disney+", packageNames.contains("com.disney.disneyplus"))
        assertTrue("Should contain YouTube TV", packageNames.contains("com.google.android.youtube.tv"))
    }

    @Test
    fun `all CuratedApps have unique IDs`() {
        val ids = CuratedApps.map { it.id }
        val uniqueIds = ids.toSet()
        
        assertEquals("All app IDs should be unique", ids.size, uniqueIds.size)
    }

    @Test
    fun `all CuratedApps have unique package names`() {
        val packageNames = CuratedApps.map { it.packageName }
        val uniquePackageNames = packageNames.toSet()
        
        assertEquals("All package names should be unique", packageNames.size, uniquePackageNames.size)
    }

    @Test
    fun `all CuratedApps have non-empty names`() {
        CuratedApps.forEach { app ->
            assertTrue("App ${app.id} should have a name", app.name.isNotEmpty())
        }
    }

    @Test
    fun `all CuratedApps have non-empty package names`() {
        CuratedApps.forEach { app ->
            assertTrue("App ${app.id} should have a package name", app.packageName.isNotEmpty())
        }
    }

    @Test
    fun `all CuratedApps have valid categories`() {
        val validCategories = setOf(
            "Streaming", "Social Media", "Live Streaming", 
            "Music & Podcasts", "Live TV", "Entertainment"
        )
        
        CuratedApps.forEach { app ->
            assertTrue(
                "App ${app.name} has invalid category: ${app.category}",
                validCategories.contains(app.category)
            )
        }
    }

    @Test
    fun `all CuratedApps have descriptions`() {
        CuratedApps.forEach { app ->
            assertTrue(
                "App ${app.name} should have a description",
                app.description.isNotEmpty()
            )
        }
    }

    @Test
    fun `all CuratedApps have ratings`() {
        CuratedApps.forEach { app ->
            assertTrue(
                "App ${app.name} should have a rating",
                app.rating.isNotEmpty()
            )
            assertTrue(
                "App ${app.name} rating should contain star emoji",
                app.rating.contains("⭐")
            )
        }
    }

    @Test
    fun `all CuratedApps have specs`() {
        CuratedApps.forEach { app ->
            assertTrue(
                "App ${app.name} should have specs",
                app.specs.isNotEmpty()
            )
        }
    }

    @Test
    fun `CuratedApps are marked as non-system apps`() {
        CuratedApps.forEach { app ->
            assertFalse(
                "Curated app ${app.name} should not be marked as system app",
                app.isSystem
            )
        }
    }

    @Test
    fun `Netflix has correct configuration`() {
        val netflix = CuratedApps.find { it.id == "netflix" }
        
        assertNotNull("Netflix should exist in curated apps", netflix)
        netflix?.let {
            assertEquals("Netflix", it.name)
            assertEquals("com.netflix.mediaclient", it.packageName)
            assertEquals("Streaming", it.category)
            assertTrue("Netflix should have UHD spec", it.specs.contains("UHD"))
        }
    }

    @Test
    fun `Prime Video has correct configuration`() {
        val primeVideo = CuratedApps.find { it.id == "prime_video" }
        
        assertNotNull("Prime Video should exist in curated apps", primeVideo)
        primeVideo?.let {
            assertEquals("Prime Video", it.name)
            assertEquals("com.amazon.amazonvideo.livingroom", it.packageName)
            assertEquals("Streaming", it.category)
        }
    }
}
