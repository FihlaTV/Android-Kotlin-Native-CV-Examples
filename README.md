# Android-Kotlin-Native-CV-Examples

This is a Android Studio Kotlin project to demo some OpenCV operations.
Some examples include native OpenCV functions and JNI calls.

## Requirement:

Android Studio 3+

OpenCV version: 3.4.1

## How to create the native OpenCV project
* Make sure you have Android SDK up to date, with NDK installed and CMake

* Download OpenCV SDK for Android from OpenCV.org and decompress the zip file

* Create a new Android Studio project
  * Check Include C++ and Kotlin support
  * Choose empty Activity
  * In C++ Support, you can check -fexceptions and -frtti

* Import OpenCV library module
  * New -> Import Module
  * Choose the `YOUR_OPENCV_SDK/sdk/java` folder
  * Unckeck replace jar, unckeck replace lib, unckeck create gradle-style

  * Set the OpenCV library module up to fit your SDK

    Edit `openCVLibrary/build.gradle` to fit your SDK:

    ```
    compileSdkVersion 28
    defaultConfig {
      minSdkVersion 25
      targetSdkVersion 28
    }
    ```

  * Add OpenCV module dependency in your app module

    * File -> Project structure -> Module app -> Dependencies tab -> New module dependency -> choose OpenCV library module


* Create a folder named `jniLibs` in `app/src/main` and copy the files from `YOUR_OPENCV_SDK/sdk/native/libs` to that folder


  * Set the app build.gradle
  
    Add abiFilters
  
    ```
    externalNativeBuild {
        cmake {
            cppFlags "-frtti -fexceptions"
            abiFilters 'x86', 'x86_64', 'armeabi-v7a', 'arm64-v8a'
        }
    }
    ```

  * Add openCV jniLibs directory to point to the libs
    ```
    sourceSets {
        main {
            jniLibs.srcDirs = ['src/main/jniLibs']
        }
    }
    ```
* Create a folder named `include` in `app/src/main/cpp/include`
and copy the header files from `YOUR_OPENCV_SDK/sdk/native/jni/include`.

* Configure the `CMakeLists.txt` file
  * After the `cmake_minimum_required`, add

    ```
    include_directories(YOUR_OPENCV_SDK/sdk/native/jni/include)
    add_library( lib_opencv SHARED IMPORTED )
    set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)
    ```

  * At the end of the file add `lib_opencv` to the `target_link_libraries` list
    ```
    target_link_libraries(
            native-lib
            lib_opencv
            ${log-lib})
    ```
* Grant camera permission
  * Add the following lines in the `AndroidManifest.xml` file

    ```
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-feature android:name="android.hardware.camera"/>
    <uses-feature android:name="android.hardware.camera.autofocus"/>
    <uses-feature android:name="android.hardware.camera.front"/>
    <uses-feature android:name="android.hardware.camera.front.autofocus"/>
    ```

## Activities