plugins {
    // For build.gradle.kts (Kotlin DSL)
    kotlin("jvm") version "2.0.0"
}

group = "com.thalkz"
version = "1.0"

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
}
