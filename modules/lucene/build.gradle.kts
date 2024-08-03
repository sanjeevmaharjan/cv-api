/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

group = "np.sanjeev"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildlogic.kotlin-library-conventions")

    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    implementation("np.sanjeev:core")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.hibernate.search:hibernate-search-mapper-orm:7.1.1.Final")
    api("org.hibernate.search:hibernate-search-backend-lucene:7.1.1.Final")
    api("io.github.oshai:kotlin-logging-jvm:7.0.0")
}
