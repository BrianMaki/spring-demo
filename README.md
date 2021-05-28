# spring-demo

Work in progress.  Readme to be updated.

This Spring Boot Demo Application uses the following:

- Postgres database
- H2 database for integration unit testing
- Mockito for unit testing
- Lombok library
- Spring Projection
- Sonarqube for code analysis
- Swagger
- Optimistic Locking example
- Customer Order Cross Reference Table JPA implementation
- Basic Controller => Service => Repository implementation with DTOs
- DTO validation via annotations

Run docker compose file to create Postgres and Sonarqube

- docker-compose up --build -d

Swagger

- http://localhost:9002/spring-demo/swagger-ui/#

PG Admin 4

- Install PG Admin 4 GUI to access Postgres database
- https://www.pgadmin.org/download/

Sonarqube

- https://docs.sonarqube.org/latest/setup/get-started-2-minutes/

Lombok

- Intellij => Lombok plugin is already pre-installed unless your version is 2020.3 or less https://projectlombok.org/setup/intellij
- Eclipse => Lombok plugin will need to be installed https://projectlombok.org/setup/eclipse

Run Spring Boot Application

- SpringDemoApplication.java
