# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep native methods and their classes to prevent JNI errors
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep members with @Keep annotation
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

# Preserve Line Numbers for useful stack traces (obfuscated)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ViewModel rules
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Data classes - Keep for potential reflection/serialization
-keep class com.tvapp.launcher.AppItem { *; }
-keep class com.tvapp.launcher.CalendarFields { *; }

# General optimizations
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
