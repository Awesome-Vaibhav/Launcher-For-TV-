package vaibhav.all.apps.launcher

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log

@RunWith(AndroidJUnit4::class)
class PerformanceBenchmarkTest {

    @Test
    fun benchmarkFiltering() {
        val systemApps = (1..5000).map {
            AppItem(
                id = "com.example.app$it",
                name = "App $it",
                packageName = "com.example.app$it",
                isSystem = true,
                brandColor = androidx.compose.ui.graphics.Color.Black,
                description = "Desc",
                category = "Category",
                rating = "4.0",
                specs = emptyList(),
                mockShow = "",
                launchIntent = null,
                iconDrawable = null
            )
        }

        val searchQuery = ""

        // Warmup
        for (i in 1..10) {
            systemApps.filter {
                NativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                        NativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
            }
        }

        val startOld = System.nanoTime()
        for (i in 1..100) {
            systemApps.filter {
                NativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                        NativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
            }
        }
        val endOld = System.nanoTime()
        Log.d("BENCHMARK", "Old approach time (ms): ${(endOld - startOld) / 1000000.0}")

        val startNew = System.nanoTime()
        for (i in 1..100) {
            if (searchQuery.isBlank()) {
                systemApps
            } else {
                systemApps.filter {
                    NativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                            NativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
                }
            }
        }
        val endNew = System.nanoTime()
        Log.d("BENCHMARK", "New approach time (ms): ${(endNew - startNew) / 1000000.0}")
    }
}
