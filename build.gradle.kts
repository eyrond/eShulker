plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("io.papermc.paperweight.userdev") version "1.5.5"
}

group = "dev.eyrond.shulker"
version = "1.0.1"

repositories {
    mavenCentral()
    // PaperMC repository.
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    implementation("org.bstats", "bstats-bukkit", "3.0.2")
    implementation("dev.eyrond.paperkt", "core", "1.0.0")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    processResources {
        expand("version" to project.version)
    }

    runServer {
        minecraftVersion("1.19.4")
    }

    shadowJar {
        dependencyFilter.apply {
            // Exclude all the dependencies that are already provided by Paper.
            exclude(dependency("org.jetbrains:annotations"))
        }
        relocate("org.bstats", "dev.eyrond.shulker.bstats")
    }

    test {
        useJUnitPlatform()
    }
}
