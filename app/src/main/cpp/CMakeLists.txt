cmake_minimum_required(VERSION 3.22.1)

project(native_utils)

add_library(
    native-utils
    SHARED
    native-utils.cpp
)

find_library(log-lib log)

target_link_libraries(
    native-utils
    ${log-lib}
)
