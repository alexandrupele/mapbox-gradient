import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    val secrets = loadSecretProperties()
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            // Do not change the username below. It should always be "mapbox" (not your username).
            credentials.username = "mapbox"
            // Use the secret token stored in gradle.properties as the password
            credentials.password = secrets.getProperty("mapboxSecretKey")
            authentication.create<BasicAuthentication>("basic")
        }
    }
}

fun loadSecretProperties() = Properties().apply {
    load(File(rootDir, "secret.properties").inputStream())
}

rootProject.name = "MapboxGradient"
include(":app")
 