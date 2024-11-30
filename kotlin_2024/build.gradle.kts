plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(18)
}
