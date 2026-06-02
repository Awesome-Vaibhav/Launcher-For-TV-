package vaibhav.all.apps.launcher

import org.junit.Test
import kotlin.system.measureTimeMillis

// Mock NativeStringMatcher just for benchmark.
object MockNativeStringMatcher {
    fun containsIgnoreCase(text: String, query: String): Boolean {
        if (query.isBlank()) return true
        return text.contains(query, ignoreCase = true)
    }
}

class PerformanceBenchmarkTest {

    @Test
    fun benchmarkFiltering() {
        val testApps = (1..1000).map { i ->
            AppItem(
                id = "id_$i",
                name = "App Name $i",
                packageName = "com.example.app$i",
                isSystem = true,
                brandColor = androidx.compose.ui.graphics.Color.Black,
                description = "",
                category = "",
                rating = "",
                specs = emptyList(),
                mockShow = "",
                launchIntent = null,
                iconDrawable = null
            )
        }

        val searchQuery = ""

        // Warmup
        for (i in 1..100) {
            testApps.filter {
                MockNativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                        MockNativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
            }
        }

        val oldTime = measureTimeMillis {
            for (i in 1..1000) {
                testApps.filter {
                    MockNativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                            MockNativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
                }
            }
        }
        println("Old approach time (searchQuery blank): $oldTime ms")

        val newTime = measureTimeMillis {
            for (i in 1..1000) {
                if (searchQuery.isBlank()) {
                    testApps
                } else {
                    testApps.filter {
                        MockNativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                                MockNativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
                    }
                }
            }
        }
        println("New approach time (searchQuery blank): $newTime ms")


        val searchQuery2 = "123"

        // Warmup
        for (i in 1..100) {
            testApps.filter {
                MockNativeStringMatcher.containsIgnoreCase(it.name, searchQuery2) ||
                        MockNativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery2)
            }
        }

        val oldTime2 = measureTimeMillis {
            for (i in 1..1000) {
                testApps.filter {
                    MockNativeStringMatcher.containsIgnoreCase(it.name, searchQuery2) ||
                            MockNativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery2)
                }
            }
        }
        println("Old approach time (searchQuery present): $oldTime2 ms")

        val newTime2 = measureTimeMillis {
            for (i in 1..1000) {
                if (searchQuery2.isBlank()) {
                    testApps
                } else {
                    testApps.filter {
                        MockNativeStringMatcher.containsIgnoreCase(it.name, searchQuery2) ||
                                MockNativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery2)
                    }
                }
            }
        }
        println("New approach time (searchQuery present): $newTime2 ms")
    }

    @Test
    fun benchmarkObjectCreation() {
        val categories = listOf("Games", "Productivity", "Social", "Entertainment", "Utilities", "Other")

        val timeOld = measureTimeMillis {
            for (i in 0..10_000_000) {
                val detectedCategory = categories[i % categories.size]
                val specs = when (detectedCategory) {
                    "Games" -> listOf("INTERACTIVE", "LOCAL GAME", "60 FPS")
                    "Productivity" -> listOf("WORKSPACE", "PRODUCTIVITY", "CLOUD DATA")
                    "Social" -> listOf("SOCIAL", "MESSAGING", "COMMUNITY")
                    "Entertainment" -> listOf("CINEMATIC", "MEDIA FEED", "UHD/HD")
                    "Utilities" -> listOf("CORE UTILITY", "SYSTEM RUNTIME")
                    else -> listOf("DEVICE SERVICE", "LOCAL SHORTCUT")
                }
                val description = when (detectedCategory) {
                    "Games" -> "Immersive gameplay application installed on this device. Start playing with zero buffering."
                    "Productivity" -> "Productivity workspace tool for managing documents, tasks, and notes on your cinematic driver."
                    "Social" -> "Communicate, message, and stay connected with your social circles directly from your TV layout."
                    "Entertainment" -> "High quality video, music, or media stream application. Cinema mode output fully supported."
                    "Utilities" -> "System helper or core administration tool to keep your hardware running smoothly."
                    else -> "Local system device application launch helper. Tap to open package directly."
                }
                val rating = when (detectedCategory) {
                    "Games" -> "4.7 ⭐"
                    "Productivity" -> "4.5 ⭐"
                    "Social" -> "4.6 ⭐"
                    "Entertainment" -> "4.8 ⭐"
                    "Utilities" -> "4.4 ⭐"
                    else -> "4.2 ⭐"
                }

                // Do something to prevent JIT from optimizing it away completely
                if (specs.size > 10) println(description)
            }
        }

        println("Old approach time (object creation): $timeOld ms")
    }
}
