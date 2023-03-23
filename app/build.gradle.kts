plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace  = "ru.anddever.currencyviewer"
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.anddever.currencyviewer"
        minSdk      = 16
        targetSdk   = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations.add("ru")
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
        debug {
            versionNameSuffix   = "-debug"
            applicationIdSuffix = ".debug"
            isMinifyEnabled     = false
            isShrinkResources   = false
        }
        create("debugMini") {
            initWith(getByName("debug"))
            versionNameSuffix   = "-debug-mini"
            applicationIdSuffix = ".debug-mini"
            isMinifyEnabled     = true
            isShrinkResources   = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules-debug.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Network
    /** Okhttp version must be 3.12.1 for supporting android lower than 4.4 */
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")
    /** Retrofit version must be 2.6.4 for supporting android lower than 5 */
    implementation("com.squareup.retrofit2:retrofit:2.6.4")
    implementation("com.squareup.retrofit2:converter-gson:2.6.4")

    // Room components
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.core:core-ktx:1.9.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    testImplementation("androidx.room:room-testing:2.5.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // WorkManager
    implementation("androidx.work:work-runtime:2.8.0")
    androidTestImplementation("androidx.work:work-testing:2.8.0")
}