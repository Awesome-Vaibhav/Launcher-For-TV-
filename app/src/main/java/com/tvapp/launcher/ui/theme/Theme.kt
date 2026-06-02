package com.tvapp.launcher.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = FireOrangePrimary,
  secondary = FireAmberAccent,
  tertiary = FireAccentTeal,
  background = FireDarkBackground,
  surface = FireSurfaceDark,
  onPrimary = FireDarkBackground,
  onSecondary = FireTextPrimary,
  onBackground = FireTextPrimary,
  onSurface = FireTextPrimary
)

private val LightColorScheme = darkColorScheme( // Enforce dark/cinematic style even on light systems for media app authenticity
  primary = FireOrangePrimary,
  secondary = FireAmberAccent,
  tertiary = FireAccentTeal,
  background = FireDarkBackground,
  surface = FireSurfaceDark,
  onPrimary = FireDarkBackground,
  onSecondary = FireTextPrimary,
  onBackground = FireTextPrimary,
  onSurface = FireTextPrimary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force cinematic dark
  dynamicColor: Boolean = false, // Disable wallpaper colors to prevent brand breakage
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme


  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
