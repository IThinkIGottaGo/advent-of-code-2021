plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
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