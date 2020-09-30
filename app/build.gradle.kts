plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    setCompileSdkVersion(30)
    buildToolsVersion = "30.0.2"
    defaultConfig {
        applicationId = "com.qianlei.jiaowu"
        setMinSdkVersion(21)
        setTargetSdkVersion(30)
        versionCode = 2
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat", "appcompat", "1.2.0")
    implementation("com.google.android.material", "material", "1.2.1")
    implementation("androidx.constraintlayout", "constraintlayout", "2.0.1")
    implementation("androidx.vectordrawable", "vectordrawable", "1.1.0")
    implementation("androidx.navigation", "navigation-fragment", "2.3.0")
    implementation("androidx.navigation", "navigation-ui", "2.3.0")
    implementation("androidx.lifecycle", "lifecycle-extensions", "2.2.0")
    implementation("androidx.lifecycle", "lifecycle-viewmodel-ktx", "2.2.0")
    implementation("androidx.lifecycle", "lifecycle-runtime-ktx", "2.2.0")
    implementation("androidx.lifecycle", "lifecycle-livedata-ktx", "2.2.0")
    implementation("androidx.preference", "preference", "1.1.1")
    implementation("androidx.legacy", "legacy-support-v4", "1.0.0")
    implementation("androidx.fragment", "fragment-ktx", "1.2.5")
    implementation("androidx.security", "security-crypto", "1.1.0-alpha02")
    implementation("androidx.room", "room-runtime", "2.2.5")
    implementation("androidx.room", "room-ktx", "2.2.5")
    kapt("androidx.room", "room-compiler", "2.2.5")
    testImplementation("junit", "junit", "4.13")
    androidTestImplementation("androidx.test.ext", "junit", "1.1.2")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "3.3.0")
    implementation("com.afollestad.material-dialogs", "datetime", "3.2.1")
    implementation("com.afollestad.material-dialogs", "core", "3.2.1")
    implementation("com.github.zfman", "TimetableView", "2.0.7")
    implementation("org.jsoup", "jsoup", "1.13.1")
    implementation("com.squareup.okhttp3", "okhttp", "4.7.2")
    implementation("com.google.code.gson", "gson", "2.8.6")
    implementation("br.com.simplepass", "loading-button-android", "2.2.0")
}
