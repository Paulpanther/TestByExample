
plugins {
    kotlin("jvm") version "1.6.20" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.6.20"))
    }
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("testbyexample")
    }
}
