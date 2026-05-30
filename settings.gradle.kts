pluginManagement {
  repositories {
    maven("https://maven.google.com") {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.android.application") {
        useModule("com.android.tools.build:gradle:${requested.version}")
      }
    }
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    maven("https://maven.google.com")
    mavenCentral()
  }
}

rootProject.name = "My Application"

include(":app")
