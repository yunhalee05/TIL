plugins {
	id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
}

ktlint {
	enableExperimentalRules.set(true)

	filter {
		exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
	}
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = true
}
