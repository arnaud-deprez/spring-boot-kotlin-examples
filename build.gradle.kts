import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	base
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71" apply false
	id("org.springframework.boot") version "2.1.6.RELEASE" apply false
	id("io.spring.dependency-management") version "1.0.7.RELEASE" apply false
}

allprojects {
	group = "com.powple"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
		jcenter()
	}
}

val junit5_version: String by project

subprojects {

	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")

	java.sourceCompatibility = JavaVersion.VERSION_1_8

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "1.8"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}