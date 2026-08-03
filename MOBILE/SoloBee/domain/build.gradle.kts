plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    // Pure-Kotlin domain layer: no Android / Compose dependencies.
    api(libs.kotlinx.coroutines.core)   // Flow / coroutineScope used in public UseCase signatures
    api(libs.javax.inject)              // @Inject constructors (JSR-330); Hilt wiring lives in app/data

    testImplementation(libs.junit)
}
