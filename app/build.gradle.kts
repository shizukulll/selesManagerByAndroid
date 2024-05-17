plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.selesmanager"
    compileSdk = 34
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.keystore")
            storePassword = "Saberlily99"

        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs["release"]

        }
    }
    defaultConfig {
        applicationId = "com.example.selesmanager"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

//    my implement
    implementation("com.squareup.okhttp3:okhttp:3.4.1")
    implementation("com.google.code.gson:gson:2.7")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
}