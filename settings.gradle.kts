pluginManagement {
    repositories {
        google()  // For Google services like Firebase
        mavenCentral()  // For dependencies hosted on Maven Central
        gradlePluginPortal()  // For Gradle Plugin Portal if you're using any plugins from there
    }
}

dependencyResolutionManagement {
    // This line makes sure that only repositories defined here will be used.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Repositories used by the project
    repositories {
        google()  // For Google services like Firebase
        mavenCentral()  // For dependencies hosted on Maven Central
    }
}

rootProject.name = "Roombasedattendance"
include(":app")
