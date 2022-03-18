pluginManagement {
    repositories {
        maven {
            url = uri("/local_maven/repos")
        }
    }
}
rootProject.name = "MyGradlePluginSample"
include ("app")
include(":delete-method-plugin")
println("settings.gradle running done")
