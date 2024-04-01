plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.thalkz"
version = "1.0-SNAPSHOT"

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
