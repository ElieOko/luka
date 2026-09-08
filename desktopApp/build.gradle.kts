import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(compose.foundation)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "elieoko.mobile.luka.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Luka"
            packageVersion = "1.0.0"
        }
    }
}

tasks.register<JavaExec>("generateScreenshots") {
    group = "verification"
    dependsOn("compileKotlinJvm")
    val compilation = kotlin.jvm().compilations.getByName("main")
    classpath = compilation.output.allOutputs + compilation.runtimeDependencyFiles
    mainClass.set("elieoko.mobile.luka.desktop.ScreenshotsKt")
    workingDir = rootProject.projectDir
    systemProperty("luka.screenshot.dir", rootProject.layout.buildDirectory.dir("screenshots").get().asFile.absolutePath)
}
