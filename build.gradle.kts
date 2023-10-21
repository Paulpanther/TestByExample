import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    kotlin("jvm") version "1.9.10" apply false
}

// Not sure if I need this
buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
    }
}

subprojects {
    apply(plugin = "kotlin")
    configure<KotlinJvmProjectExtension> {
        jvmToolchain(11)
    }
}
