plugins {
    kotlin("jvm") version "1.4.30"
    application
    id("org.openapi.generator") version "5.0.1"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/resources/swagger.yaml")
    outputDir.set("$buildDir/generated/openapi")
    library.set("jvm-retrofit2")
    packageName.set("ru.tinkoff.invest.openapi")
    configOptions.apply {
        put("useCoroutines", "true")
        put("serializableModel", "true")
        put("serializationLibrary", "jackson")
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
