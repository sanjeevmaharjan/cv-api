pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("../build-logic")
}

rootProject.name = "modules"

file(".").listFiles()
    .filter { moduleBuild -> moduleBuild.isDirectory() && !moduleBuild.name.startsWith(".") }
    .forEach { moduleBuild: File ->
        include(moduleBuild.name)
    }
