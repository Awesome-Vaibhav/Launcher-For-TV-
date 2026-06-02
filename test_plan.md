1. **Change the caught exception type in `NativeStringMatcher.kt`**
   - The current code catches `Exception`, but `UnsatisfiedLinkError` extends `Error`, not `Exception`. That's why the test fails. We need to catch `Throwable` (or `Error` as well).
2. **Update the test `NativeStringMatcherTest.kt`**
   - We will write tests to properly verify the fallback behavior of `NativeStringMatcher`.
   - We will use reflection to set `isNativeLibraryLoaded` to `true` (to trigger fallback when native is missing).
   - We will also add tests where `isNativeLibraryLoaded` is `false`.
3. **Execute tests**
   - Verify that tests run and pass correctly.
