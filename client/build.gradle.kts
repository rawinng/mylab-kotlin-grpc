import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
}

group = "rawinng"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":stub"))
    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")

    testImplementation(kotlin("test"))
}

tasks.register<JavaExec>("HelloWorldClient") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("rawinng.hellogrpc.client.HelloWorldClientKt")
}

val helloWorldClientStartScripts = tasks.register<CreateStartScripts>("helloWorldClientStartScripts") {
    mainClass.set("io.grpc.examples.helloworld.HelloWorldClientKt")
    applicationName = "hello-world-client"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

tasks.named("startScripts") {
    dependsOn(helloWorldClientStartScripts)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}