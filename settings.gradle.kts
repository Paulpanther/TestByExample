pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.9.10-1.0.13"
        kotlin("jvm") version "1.9.10"
    }
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TestByExample"
include("lib")
include("processor")
include("annotations")
include("attach")
include("common")

