package vaibhav.all.apps.launcher

import android.content.pm.ApplicationInfo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {

    @Test
    fun testCategoryClassification_GamesByFlag() {
        val appInfo = ApplicationInfo().apply {
            flags = ApplicationInfo.FLAG_IS_GAME
        }
        val category = AppCategoryClassifier.getCategory("com.android.mine", "Minecraft", appInfo)
        assertEquals("Games", category)
    }

    @Test
    fun testCategoryClassification_ProductivityByKeywords() {
        val appInfo = ApplicationInfo()
        val category = AppCategoryClassifier.getCategory("com.microsoft.office.word", "Document Reader", appInfo)
        assertEquals("Productivity", category)
    }

    @Test
    fun testCategoryClassification_SocialByKeywords() {
        val appInfo = ApplicationInfo()
        val category = AppCategoryClassifier.getCategory("com.whatsapp", "WhatsApp Chat", appInfo)
        assertEquals("Social", category)
    }

    @Test
    fun testCategoryClassification_EntertainmentByKeywords() {
        val appInfo = ApplicationInfo()
        val category = AppCategoryClassifier.getCategory("com.netflix.mediaclient", "Netflix Main", appInfo)
        assertEquals("Entertainment", category)
    }

    @Test
    fun testCategoryClassification_UtilitiesByKeywords() {
        val appInfo = ApplicationInfo()
        val category = AppCategoryClassifier.getCategory("com.android.settings", "Settings Menu", appInfo)
        assertEquals("Utilities", category)
    }

    @Test
    fun testCategoryClassification_UncategorizedToOther() {
        val appInfo = ApplicationInfo()
        val category = AppCategoryClassifier.getCategory("com.random.unrecognized.app", "Unknown Widget", appInfo)
        assertEquals("Other", category)
    }
}
