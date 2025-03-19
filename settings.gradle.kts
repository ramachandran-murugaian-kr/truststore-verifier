pluginManagement {
    repositories {
        maven {
            url = uri("https://artifactory-edge.kroger.com/artifactory/plugins-release")
            credentials {
                username = System.getenv("ARTIFACTORY_USER")
                password = System.getenv("ARTIFACTORY_EDGE_PASSWORD")
            }
        }
        maven {
            url = uri("https://artifactory-edge.kroger.com/artifactory/gradle-plugins-cache")
            credentials {
                username = System.getenv("ARTIFACTORY_USER")
                password = System.getenv("ARTIFACTORY_EDGE_PASSWORD")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "truststore-verifier"