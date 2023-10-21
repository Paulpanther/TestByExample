plugins {
    application
}

group = "com.testbyexample"
version = "1.0"

sourceSets.getByName("test") {
    java.srcDir("src/main/java")
}

dependencies {
    annotationProcessor(project(":processor"))
    implementation(project(":annotations"))
    implementation("org.junit.platform:junit-platform-launcher:1.8.2")
    implementation("org.junit.platform:junit-platform-runner:1.8.2")
    implementation("org.junit.platform:junit-platform-engine:1.8.2")
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("testbyexample")
    }
}
