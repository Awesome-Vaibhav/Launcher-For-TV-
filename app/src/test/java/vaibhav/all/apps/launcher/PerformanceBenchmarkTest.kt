package vaibhav.all.apps.launcher

import org.junit.Test
import kotlin.system.measureTimeMillis

class PerformanceBenchmarkTest {
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
            }
        }

        val specsGames = listOf("INTERACTIVE", "LOCAL GAME", "60 FPS")
        val specsProductivity = listOf("WORKSPACE", "PRODUCTIVITY", "CLOUD DATA")
        val specsSocial = listOf("SOCIAL", "MESSAGING", "COMMUNITY")
        val specsEntertainment = listOf("CINEMATIC", "MEDIA FEED", "UHD/HD")
        val specsUtilities = listOf("CORE UTILITY", "SYSTEM RUNTIME")
        val specsOther = listOf("DEVICE SERVICE", "LOCAL SHORTCUT")

        val timeNew = measureTimeMillis {
            for (i in 0..10_000_000) {
                val detectedCategory = categories[i % categories.size]
                val specs = when (detectedCategory) {
                    "Games" -> specsGames
                    "Productivity" -> specsProductivity
                    "Social" -> specsSocial
                    "Entertainment" -> specsEntertainment
                    "Utilities" -> specsUtilities
                    else -> specsOther
                }
            }
        }

        println("--------------------------------------------------")
        println("Baseline (listOf in loop): $timeOld ms")
        println("Optimized (pre-allocated): $timeNew ms")
        println("--------------------------------------------------")
    }
}
