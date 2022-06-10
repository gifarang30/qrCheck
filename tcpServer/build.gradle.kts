import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "com.qrCheck.tcpServer"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.2")
    implementation("org.openlabtesting.netty:netty-all:4.1.48.Final")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("org.lazyluke:log4jdbc-remix:0.2.7")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.apache.commons:commons-dbcp2:2.9.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.0.3")
    testImplementation("ch.qos.logback:logback-classic:1.2.11")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.jar {
    manifest.attributes["Main-Class"] = "com.qrCheck.tcpServer.TcpServer"
    manifest.attributes["Class-Path"] = configurations
        .runtimeClasspath
        .get()
        .joinToString(separator = " ") { file ->
            "libs/${file.name}"
        }
}