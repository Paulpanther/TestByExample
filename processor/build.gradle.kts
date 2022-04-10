plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":annotations"))
//    implementation("com.google.auto.service:auto-service:1.0.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.20-1.0.5")
}
