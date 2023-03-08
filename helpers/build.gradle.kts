plugins {
    base
    `java-library`
    scala
    idea
    id("com.github.johnrengelman.shadow")
    id("com.bmuschko.docker-remote-api")
}

group = "pl.touk.nussknacker"

repositories {
    //this is where NU artifacts are stored
    maven("https://oss.sonatype.org/content/groups/public/")

    mavenCentral()
}

val nussknackerVersion: String by rootProject.extra

dependencies {
    implementation(platform("pl.touk.nussknacker:nussknacker-bom_2.12:${nussknackerVersion}"))
    compileOnly("org.scala-lang:scala-library")
    testImplementation("org.scala-lang:scala-library")
    compileOnly("pl.touk.nussknacker:nussknacker-extensions-api_2.12")
}

tasks.test {
    useJUnitPlatform()
}
