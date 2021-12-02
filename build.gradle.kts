plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

@Suppress("GradlePackageUpdate")
dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
}

val javaVersion = "9"
val ktVersion = "1.6"

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = javaVersion
            apiVersion = ktVersion
            languageVersion = ktVersion
        }
    }
    wrapper {
        gradleVersion = "7.3"
    }
}