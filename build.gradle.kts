plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "eu.stephanson.external"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")

}

tasks.named("run", JavaExec::class.java) {
    workingDir = File("${buildDir}/content").apply(File::mkdirs)
}