import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

group = "com.testbyexample"
version = "1.0"

dependencies {
    implementation("org.javassist:javassist:3.29.2-GA")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("attach.jar")

    manifest {
        attributes["Main-Class"] = "com.testbyexample.VMStarterKt"
        attributes["Agent-Class"] = "com.testbyexample.ProbeAgent"
        attributes["Can-Retransform-Classes"] = "true"
        attributes["Can-Redefine-Classes"] = "true"
    }
}
