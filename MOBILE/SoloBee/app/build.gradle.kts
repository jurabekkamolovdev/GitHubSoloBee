plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)           // Hilt uchun
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.gms)
    alias(libs.plugins.firebase.crashlytics)
}
android {
    namespace = "com.javohir.solobee"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.javohir.solobee"
        minSdk = 24
        targetSdk = 36
        versionCode = 4
        versionName = "2"

        testInstrumentationRunner = "com.javohir.solobee.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // ProGuard vs R8 disabled
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    lint {
        abortOnError = true
        disable += "AndroidGradlePluginVersion"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Modullar bog'lanishi Javohir Oromov
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature"))
    implementation(project(":core:utils"))
    implementation(project(":core:ui"))

    // Hilt Javohir Oromov
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Screen Animatsiya
    implementation(libs.androidx.navigation.compose)

    // Firebase Crashlytics Javohir Oromov
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

     // WindowSizeClass
    implementation(libs.androidx.compose.material3.window.size.class1)

    // Unit testing Javohir Oromov
    testImplementation(libs.kotlinx.coroutines.test)

    // UI test
    androidTestImplementation(libs.hilt.android.testing)
}