plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("BitcoinCorePlugin") {
            id = "bisq.gradle.bitcoin_core.BitcoinCorePlugin"
            implementationClass = "bisq.gradle.bitcoin_core.BitcoinCorePlugin"
        }
    }
}

dependencies {
    implementation(project(":gradle-tasks"))
    implementation(libs.google.guava)
}