import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

plugins {
    base
    `java-library`
    scala
    idea
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.bmuschko.docker-remote-api") version "7.0.0"
}

val scalaVersion = "2.12.10"

group = "pl.touk.nussknacker"

var nussknackerVersion: String by extra
ext {
    val versionFromEnv = System.getenv()["NUSSKNACKER_VERSION"]
    if (versionFromEnv.isNullOrBlank()) {
        nussknackerVersion = File("./nussknacker.version").readText(Charsets.UTF_8).trim()
    } else {
        nussknackerVersion = versionFromEnv
    }
}
println("Nussknacker version: $nussknackerVersion")

repositories {
    maven("https://oss.sonatype.org/content/groups/public/")
    mavenCentral()
    maven("https://packages.confluent.io/maven")
}

//todo: ugly but works with subproject
tasks.register<Copy>("copyFatJarToDockerDir") {
    dependsOn(tasks.shadowJar)
    from(file("helpers/build/lib/helpers-all.jar"))
    into(file("docker"))
}

fun commonDockerAction(from: String, target: String, task: DockerBuildImage) {
    with(task) {
        dependsOn("copyFatJarToDockerDir")
        inputDir.set(file("docker"))
        images.add("$target:latest")
        buildArgs.putAll(mapOf(
            "NU_IMAGE" to "$from:${nussknackerVersion}_scala-2.12"
        ))
    }
}

tasks.create("buildDesignerImage", DockerBuildImage::class) {
    commonDockerAction("touk/nussknacker", rootProject.name, this)
}
tasks.create("buildLiteRuntimeAppImage", DockerBuildImage::class) {
    commonDockerAction("touk/nussknacker-lite-runtime-app", "${rootProject.name}-lite-runtime-app", this)
}

tasks.create("buildImages", DefaultTask::class) {
    dependsOn("buildDesignerImage", "buildLiteRuntimeAppImage")
}

tasks.test {
    useJUnitPlatform()
}
