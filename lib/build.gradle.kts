
//pluginManagement {
//    repositories {
//        gradlePluginPortal()
//    }
//}

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.6.20-1.0.5"
    application
}

group = "com.testbyexample"
version = "1.0"

ksp {
    arg("ignoreGenericArgs", "false")
}

dependencies {
    ksp(project(":processor"))
    implementation(project(":annotations"))
    implementation("org.junit.platform:junit-platform-launcher:1.8.2")
    implementation("org.junit.platform:junit-platform-runner:1.8.2")
    implementation("org.junit.platform:junit-platform-engine:1.8.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}
