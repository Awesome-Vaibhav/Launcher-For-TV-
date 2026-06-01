# Unit Tests

This directory contains unit tests for the Launcher-For-TV Android application.

## Testing Philosophy

Following modern Android testing best practices (2026), we focus on:

1. **Fast, Pure Unit Tests** - No Android framework dependencies, runs in milliseconds
2. **Data Integrity** - Verify data models and business logic
3. **Regression Protection** - Catch bugs before they reach production
4. **CI/CD Integration** - All tests run automatically on every release

## Test Coverage

### 1. CuratedAppsTest.kt
Tests the integrity of the curated streaming apps list:
- ✅ List is not empty
- ✅ Contains expected streaming services (Netflix, Prime Video, Disney+, etc.)
- ✅ All apps have unique IDs and package names
- ✅ All apps have valid names, descriptions, ratings, and specs
- ✅ All apps are categorized correctly
- ✅ Specific app configurations (Netflix, Prime Video)

**Why it matters**: Ensures the app launches with valid data and prevents configuration errors.

### 2. AppItemTest.kt
Tests the AppItem data model:
- ✅ Can be created with required fields
- ✅ Has correct default values
- ✅ Supports custom values
- ✅ Equality and copy operations work correctly

**Why it matters**: Validates the core data structure used throughout the app.

### 3. CalendarFieldsTest.kt
Tests the CalendarFields data class:
- ✅ Can be created with valid date/time values
- ✅ Handles edge cases (midnight, end of day)
- ✅ Equality and copy operations work correctly

**Why it matters**: Ensures the clock widget displays correct time information.

### 4. AppCategoryClassifierTest.kt
Tests the automatic app categorization logic:
- ✅ Correctly identifies Games
- ✅ Correctly identifies Productivity apps
- ✅ Correctly identifies Social apps
- ✅ Correctly identifies Entertainment/Streaming apps
- ✅ Correctly identifies Utilities
- ✅ Returns "Other" for unrecognized apps
- ✅ Case-insensitive matching
- ✅ Handles null appInfo gracefully

**Why it matters**: Ensures apps are properly categorized for users to find them easily.

### 5. StringMatchingTest.kt
Tests string search functionality:
- ✅ Case-insensitive search works correctly
- ✅ Partial matches work
- ✅ Empty query matches everything
- ✅ Special characters are handled
- ✅ Package name search works

**Why it matters**: Ensures the app search feature works reliably.

## Running Tests

### Locally
```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Run with coverage
./gradlew testDebugUnitTestCoverage

# Run specific test class
./gradlew testDebugUnitTest --tests "vaibhav.all.apps.launcher.CuratedAppsTest"
```

### In CI/CD
Tests run automatically on every tag push via GitHub Actions:
```bash
git tag v1.0.0
git push origin v1.0.0
```

## Test Results
- Test reports: `app/build/reports/tests/testDebugUnitTest/index.html`
- Coverage reports: `app/build/reports/coverage/`

## Why No Robolectric?

We use **pure JUnit tests** instead of Robolectric because:
1. ⚡ **10-100x faster** - No Android framework overhead
2. 🎯 **More reliable** - No SDK compatibility issues in CI
3. 🧪 **Better isolation** - Tests only business logic, not Android internals
4. 🚀 **CI-friendly** - Works perfectly in GitHub Actions without special setup

For UI testing, we recommend:
- **Instrumented tests** with Espresso/Compose UI Testing (on real devices)
- **Screenshot tests** with Roborazzi (when SDK support improves)

## Adding New Tests

When adding features, write tests for:
1. **Data models** - Verify structure and defaults
2. **Business logic** - Categorization, filtering, sorting
3. **Utility functions** - String manipulation, date formatting
4. **Edge cases** - Null values, empty lists, boundary conditions

**Example:**
```kotlin
@Test
fun `new feature works correctly`() {
    // Arrange
    val input = "test"
    
    // Act
    val result = myFunction(input)
    
    // Assert
    assertEquals("expected", result)
}
```

## Best Practices

✅ **DO:**
- Write fast, isolated unit tests
- Test one thing per test
- Use descriptive test names with backticks
- Test edge cases and error conditions
- Keep tests simple and readable

❌ **DON'T:**
- Test Android framework code
- Write tests that depend on other tests
- Use Thread.sleep() or delays
- Test implementation details
- Write flaky tests

## Resources

- [Android Testing Best Practices](https://developer.android.com/training/testing)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Modern Android Testing Guide (2026)](https://developer.android.com/training/testing/fundamentals)
