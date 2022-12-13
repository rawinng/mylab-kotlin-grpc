rootProject.name = "lab-kotlin-grpc"

include("client", "server", "protos", "stub")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}