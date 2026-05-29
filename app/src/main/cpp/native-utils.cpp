#include <jni.h>
#include <algorithm>
#include <cctype>
#include <string>

namespace {
std::string ToLowerAscii(std::string value) {
    std::transform(value.begin(), value.end(), value.begin(), [](unsigned char c) {
        return static_cast<char>(std::tolower(c));
    });
    return value;
}
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_NativeStringMatcher_containsIgnoreCaseNative(
        JNIEnv* env,
        jobject /* thiz */,
        jstring text,
        jstring query) {
    if (text == nullptr || query == nullptr) {
        return JNI_FALSE;
    }

    const char* textChars = env->GetStringUTFChars(text, nullptr);
    const char* queryChars = env->GetStringUTFChars(query, nullptr);

    if (textChars == nullptr || queryChars == nullptr) {
        if (textChars != nullptr) {
            env->ReleaseStringUTFChars(text, textChars);
        }
        if (queryChars != nullptr) {
            env->ReleaseStringUTFChars(query, queryChars);
        }
        return JNI_FALSE;
    }

    std::string textLower = ToLowerAscii(textChars);
    std::string queryLower = ToLowerAscii(queryChars);

    env->ReleaseStringUTFChars(text, textChars);
    env->ReleaseStringUTFChars(query, queryChars);

    if (queryLower.empty()) {
        return JNI_TRUE;
    }

    return textLower.find(queryLower) != std::string::npos ? JNI_TRUE : JNI_FALSE;
}
