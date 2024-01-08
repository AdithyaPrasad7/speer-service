# Spring Boot 3.2.1 Project with Java 17

This is a Spring Boot project built with Java 17.
The project is written with kotlin programming language. 

## Frameworks and Libraries

- **Spring Boot 3.2.1:** The core framework for building Java-based enterprise applications.
- **Spring Web:** Provides features for building web applications.
- **Spring Data JPA:** Simplifies database access using JPA.
- **Spring Boot Actuator:** Provides production-ready features like health checks and monitoring.
- **Jackson Module Kotlin:** Adds support for Kotlin data classes in Jackson.
- **Flyway Database Migration:** Manages database schema evolution.
- **Kotlin Reflect:** Used for reflection in Kotlin.
- **Postgres Database:** An effective and popular database for development and testing purposes.
- **Spring Security:** Provides security features for the application.
- **Resilience4j:** Implements rate-limiting and resilience patterns.
- **JUnit 5:** Testing framework for unit and integration tests.
- **Hamcrest:** Matchers library for more expressive tests.
- **Mockito Kotlin:** Mocking framework for Kotlin.

## Prerequisites

- Java 17 installed
- Gradle build tool (recommended)

## Setup
Install IDE of your choice IntelliJ(recommended)
1. Clone the repository:

   git clone https://github.com/AdithyaPrasad7/speer-service.git
   cd spring-boot-java17-template

## Building the project
    ./gradlew build

## Running the Application

1. Go to /database folder and run the docker-compose file 
    docker compose up -d
or Customize the application.properties file in the src/main/resources folder for database configuration.
    
2. Run the Spring Boot application:
    ./gradlew bootRun
The application will be accessible at http://localhost:8080.

## Running Tests

    ./gradlew test

## Or to run the project without any hassel
Go to /deploy folder and run the docker-compose file
    docker compose up --build -d
