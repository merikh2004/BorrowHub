plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.borrowhub"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.borrowhub"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Local Development BASE_URL
        buildConfigField("String", "BASE_URL", "\"http://127.0.0.1:8000/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Production/Release BASE_URL
            buildConfigField("String", "BASE_URL", "\"https://127.0.0.1:8000/\"")
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        // Explicitly define mock behavior for all dependencies.
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    
    // Retrofit (since ApiService uses it, we should make sure it's here if missing)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.core.testing)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}