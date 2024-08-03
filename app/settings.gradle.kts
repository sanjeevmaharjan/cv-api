pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("../build-logic")
}

rootProject.name = "app"

include("identity", "api", "common")
