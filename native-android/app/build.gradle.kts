plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.pomodoro.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pomodoro.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Flutter 模块 — Add-to-App 核心集成点
    // 面试话术：
    //   "Flutter 模块编译为 AAR，原生壳通过 gradle 依赖引入，
    //   在 Application 层缓存 FlutterEngine，避免页面切换重建引擎。"
    debugImplementation(project(":flutter"))
    releaseImplementation(project(":flutter"))

    // Jetpack Compose — 原生壳的声明式 UI
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.10.0")

    // Navigation Compose — 4 级导航演示（对标 legacy/jetpack-android）
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // AppCompat — FlutterContainerActivity 基类
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Material Components — Theme.Material3.Light.NoActionBar 需要
    implementation("com.google.android.material:material:1.12.0")
}
