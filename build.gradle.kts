import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    `java-library`
    kotlin("jvm") version "1.4.30"
    id("org.openapi.generator") version "5.0.1"

    `maven-publish`
}

version = "0.0.1"
group = "dev.mazuninky"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/resources/swagger.yaml")
    outputDir.set("$buildDir/generated/openapi")
    library.set("jvm-okhttp4")
    packageName.set("ru.tinkoff.invest.openapi")
    configOptions.apply {
        put("serializableModel", "true")
        put("serializationLibrary", "moshi")
    }
}

sourceSets {
    main {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("$buildDir/generated/openapi/src/main/kotlin")
        }
    }
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
        kotlinOptions {
            jvmTarget = "1.8"
            sourceCompatibility = "1.8"
            targetCompatibility = "1.8"
        }
    }
}

data class Credentials(val username: String, val password: String)

fun readCredentialsFromFromEnv(): Credentials? {
    val username = System.getenv("PUBLISH_USER")
    val password = System.getenv("PUBLISH_PASSWORD")
    return if (username != null && password != null) {
        Credentials(username, password)
    } else {
        null
    }
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).allSource)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks["javadoc"])
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }

    repositories {
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/mazuninky/invest-openapi-kotlin-api")
            readCredentialsFromFromEnv()?.let {
                credentials.username = it.username
                credentials.password = it.password
            }
        }
    }
}
