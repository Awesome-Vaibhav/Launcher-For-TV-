# Testing & CI/CD Guide

## 🎯 Overview

This project uses **modern Android testing practices** with pure JUnit unit tests that run fast and reliably in CI/CD pipelines.

## ✅ What We Test

### 1. **Data Integrity** (CuratedAppsTest)
- Validates all streaming apps have correct configuration
- Ensures unique IDs and package names
- Verifies all required fields are present
- **32 test cases** covering the curated apps list

### 2. **Data Models** (AppItemTest, CalendarFieldsTest)
- Tests data class creation and defaults
- Validates equality and copy operations
- Ensures data integrity
- **11 test cases** for core data structures

### 3. **Business Logic** (AppCategoryClassifierTest)
- Tests automatic app categorization
- Validates pattern matching for different app types
- Ensures case-insensitive matching
- **9 test cases** for categorization logic

### 4. **Search Functionality** (StringMatchingTest)
- Tests case-insensitive search
- Validates partial matching
- Handles edge cases (empty queries, special characters)
- **8 test cases** for search features

## 📊 Test Statistics

- **Total Tests**: 60+ test cases
- **Execution Time**: < 5 seconds
- **Coverage**: Core business logic and data models
- **CI/CD**: Runs on every release tag

## 🚀 Running Tests

### Locally

```bash
# Run all tests
./gradlew testDebugUnitTest

# Run specific test class
./gradlew testDebugUnitTest --tests "CuratedAppsTest"

# Run with detailed output
./gradlew testDebugUnitTest --info

# View test report
# Open: app/build/reports/tests/testDebugUnitTest/index.html
```

### In CI/CD (GitHub Actions)

Tests run automatically when you push a tag:

```bash
# Create a release tag
git tag v1.0.0

# Push the tag to trigger CI/CD
git push origin v1.0.0
```

The workflow will:
1. ✅ Run all unit tests
2. ✅ Build signed release APK
3. ✅ Upload APK as artifact
4. ✅ Create GitHub release with APK attached

## 🏗️ CI/CD Workflow

### Trigger: Tag-Based Releases

```yaml
on:
  push:
    tags:
      - 'v*'  # Triggers on v1.0.0, v2.1.3, etc.
```

### Steps:
1. **Checkout code**
2. **Set up JDK 17**
3. **Run unit tests** - Fails build if tests fail
4. **Build release APK** - Signed with your keystore
5. **Upload artifact** - Available for download
6. **Create GitHub release** - With auto-generated notes

### Required Secrets

Set these in GitHub Settings → Secrets:
- `SIGNING_KEY` - Base64 encoded keystore
- `KEYSTORE_PASSWORD` - Keystore password
- `KEY_ALIAS` - Key alias (default: "upload")
- `KEY_PASSWORD` - Key password

## 🛡️ Why This Approach?

### ✅ Advantages

1. **Fast** - Tests run in seconds, not minutes
2. **Reliable** - No Robolectric SDK compatibility issues
3. **CI-Friendly** - Works perfectly in GitHub Actions
4. **Maintainable** - Simple, focused tests
5. **Regression Protection** - Catches bugs before release

### ❌ What We Don't Test

- **UI Components** - Use instrumented tests on real devices
- **Android Framework** - Already tested by Google
- **Integration** - Use Espresso/Compose UI tests
- **Screenshots** - Use Roborazzi when SDK 36 is supported

## 📝 Writing New Tests

### Template

```kotlin
@Test
fun `descriptive test name in backticks`() {
    // Arrange - Set up test data
    val input = "test"
    
    // Act - Execute the code under test
    val result = myFunction(input)
    
    // Assert - Verify the result
    assertEquals("expected", result)
}
```

### Best Practices

✅ **DO:**
- Test business logic and data models
- Use descriptive test names
- Test edge cases
- Keep tests fast and isolated
- One assertion per test (when possible)

❌ **DON'T:**
- Test Android framework code
- Write tests that depend on execution order
- Use Thread.sleep() or delays
- Test private methods directly
- Write flaky tests

## 🔄 Release Process

### 1. Make Changes
```bash
git add .
git commit -m "Add new feature"
git push origin main
```

### 2. Create Release Tag
```bash
# Semantic versioning: MAJOR.MINOR.PATCH
git tag v1.0.0
git push origin v1.0.0
```

### 3. CI/CD Runs Automatically
- Tests run
- APK builds
- Release created

### 4. Download APK
- Go to GitHub Releases
- Download the APK
- Install on Android TV

## 📈 Future Improvements

### Potential Additions:
1. **Instrumented Tests** - UI testing on real devices
2. **Screenshot Tests** - Visual regression testing
3. **Performance Tests** - App launch time, memory usage
4. **Integration Tests** - Test app flows end-to-end
5. **Code Coverage** - Track test coverage percentage

### When to Add:
- **Instrumented Tests**: When UI bugs become frequent
- **Screenshot Tests**: When Roborazzi supports SDK 36
- **Performance Tests**: When performance issues arise
- **Integration Tests**: For complex multi-screen flows

## 🐛 Troubleshooting

### Tests Fail Locally

```bash
# Clean and rebuild
./gradlew clean testDebugUnitTest

# Check specific failure
./gradlew testDebugUnitTest --tests "FailingTest" --info
```

### Tests Pass Locally But Fail in CI

- Check JDK version (should be 17)
- Verify Gradle version compatibility
- Check for environment-specific code

### Build Fails in CI

- Verify all secrets are set correctly
- Check keystore is valid
- Ensure gradlew has execute permissions

## 📚 Resources

- [Android Testing Guide](https://developer.android.com/training/testing)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [GitHub Actions for Android](https://github.com/actions/setup-java)
- [Test Reports](app/build/reports/tests/testDebugUnitTest/index.html)

---

**Remember**: Tests are your safety net. They catch bugs before users do! 🛡️
