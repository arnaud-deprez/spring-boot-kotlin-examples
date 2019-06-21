dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude("junit", "junit")
	}
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.squareup.okhttp3:mockwebserver:3.14.2")
	testImplementation("com.github.tomakehurst:wiremock-jre8:2.23.2")
}
