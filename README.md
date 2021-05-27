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
