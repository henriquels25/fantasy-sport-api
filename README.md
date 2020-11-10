[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=code_smells)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=coverage)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=security_rating)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=sqale_index)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=henriquels25_fantasy-sport-api&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api)

Fantasy Sport API
===============
Application to manage Fantasy Sport games, enabling the users to search players, pick their squads
and participate in championships.

## Purpose
This application is for educational purposes and the goal is to show good practices of
software architecture and testing.

## Technology
This project is written using the [JDK 15](https://jdk.java.net/15/) Platform,
 [Spring Boot](https://spring.io/projects/spring-boot) with the reactive web framework
 [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) 
 and [MongoDB](https://www.mongodb.com/) as the database. 

## Architecture
This application uses the [Hexagonal Architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
as the architectural style.

Following this, the application is split in three parts:

* **Business Logic (Core)**: Code that contains pure business logic, with minimal coupling
to frameworks, databases or messaging systems. Whenever the application needs to 
communicate with the external world, the core should depend on an interface that is implemented by
a secondary adapter.

* **Primary adapters**: Code that drives the application. The primary adapters should
depend on primary ports defined on the business logic.

* **Secondary adapters**: Code that enables the application to talk to external world. Secondary
adapters should implement secondary ports defined in the application core.

### Package organization
The project is split into modules according to business use cases. Each module 
has its own package inside the root package `io.henriquels25.fantasysport`.
Example:
The module responsible for managing the players is `io.henriquels25.fantasysport.player`.

Inside a module, the code related to the business logic (core) is put in the
root of the package.
Example:
The `PlayerFacade` is the entry point for the application core for the Player Module and it
is placed on `io.henriquels25.fantasysport.player`.

The adapters are placed in the package called `infra` inside the module.
Example:
The adapters for the player module are placed on `io.henriquels25.fantasysport.player.infra`.

The adapters are placed on packages according to their types. Controllers are put on the
package `**.infra.controller`, adapters to Mongo documents on `**.infra.mongo`, etc.

Primary adapters should depend on an entry point to the module business logic (core).
Example:
The primary adapter `PlayerController` depends on the `PlayerFacade` (entry point to the business logic).

Secondary adapters should implement output ports that are defined in the module business logic (core).
Example:
The secondary adapter `MongoPlayerRepository` implements the secondary port `PlayerRepository`.

## Testing
This project has three levels of testing:

* **Unit test**: Cover the business logic of the modules. They are extremely fast and do not depend
on any framework (other than JUnit5/Mockito), database or messaging system.

* **Integration test**: Cover the primary and secondary adapters of the modules. Everything that is inside the package 
`**.infra` may be tested in an integration test. These tests are slower than unit tests as it is needed an application context,
 a database or other technologies.
 In this kind of test, it should be setup only what is needed to. For example, when testing a web controller, only components
 related to the controller being tested should be configured.
 Usually, annotation like `@WebFluxTest`, `@DataMongoTest` and others are used.
 
* **Acceptance test**: This kind of test configure the whole application with the external dependencies
 (Database, Messaging Systems, External APIs) and the tests are written from the client perspective, as use cases.
 Usually, the annotation `@SpringBootTest` is used for this kind of test.

For code coverage purposes, only the unit and integration tests should be considered.

### Special Annotations
In order to have separate tasks for each kind of test, the following annotations should be used
as the test annotation:

* **Unit Test**: `@Test` (org.junit.jupiter.api.Test)
* **Integration Test**: `@IntegrationTest` (io.henriquels25.fantasysport.annotations.IntegrationTest)
* **Acceptance test**: `@AcceptanceTest` (io.henriquels25.fantasysport.annotations.AcceptanceTest)

### Gradle Tasks
The following gradle tasks are available to run the tests:
* **Unit Test**: `./gradlew unitTest`
* **Integration Test**: `./gradlew integrationTest`
* **Acceptance test**: `./gradlew acceptanceTest`

### Code Coverage
The [JaCoCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html) is configured in the project
to enable code coverage analysis.

The command `./gradlew jacocoTestReport` will generate HTML/XML code coverage reports of the executed tests.

The reports are available at the directory `build/reports/jacoco`.

For example, in order to view the coverage from the unit and integration tests combined, the following commands should be executed:

- `./gradlew unitTest`
- `./gradlew integrationTest`
- `./gradlew jacocoTestReport`

## Sonarcloud
This project is available on [Sonarcloud](https://sonarcloud.io/dashboard?id=henriquels25_fantasy-sport-api).

## Pipeline
This repository uses [GitHub Actions](https://docs.github.com/en/free-pro-team@latest/actions) to automate 
the CI/CD pipeline.

Currently, the pipeline executes for pushes in every branch, executing the following steps:
- Unit tests
- Integration tests
- Coverage report and Sonar analysis
- Acceptance Tests