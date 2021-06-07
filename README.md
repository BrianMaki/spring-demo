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
- Keycloak - secured all end points except 'get customers' and 'get orders'
- Keycloak - created realm and users
- Spring Active Profile

Run docker compose file to create Postgres and Sonarqube

- docker-compose up --build -d

PG Admin 4

- Install PG Admin 4 GUI to access Postgres database
- https://www.pgadmin.org/download/

Sonarqube

- https://docs.sonarqube.org/latest/setup/get-started-2-minutes/

mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<the-generated-token>

Lombok

- Intellij => Lombok plugin is already pre-installed unless your version is 2020.3 or less https://projectlombok.org/setup/intellij
- Eclipse => Lombok plugin will need to be installed https://projectlombok.org/setup/eclipse

Keycloack:

- http://localhost:8081 - log in with admin/admin
- Go to spring-demo realm and get credential secret for spring-demo-api
- Right click SpringDemoApplication.java and go Run Configuration. Create environment variable KEYCLOAK_CREDENTIALS_SECRET
  with credential secret for spring-demo-api.
- Get credential secret for spring-demo-developer.  This secret will be used when you get JWT later on when using Postman.

- Realm: spring-demo
- Client: spring-demo-api, spring-demo-developer, etc.

Run Spring Boot Application

- Create Environment Variable 'SPRING_PROFILES_ACTIVE'.  Value 'dev' will point to application-dev.yaml file. 
  Do not use 'unit-test'. This profile is used solely for unit tests and should not be used on start up.  If you enter a
  value that does not correspond to en existing yaml file, then application.yaml will be used.  For example, 'prod'
  will default to application.yaml file.

- SpringDemoApplication.java

Use Postman to call Customer and Order endpoints

- https://www.postman.com/downloads/

- In Postman import spring-demo.postman_environment.json located at demo/postman
- In Postman import spring-demo-api.postman_collection.json

- First use get a JWT Token. Take note of the Token if you choose to use Swagger.  
  Otherwise, this script will pre-populate JWT environment variable.  

  Note: Token will expire in 5 minutes.

- After getting a JWT Token, you can now call the other end points defined. 

  Note: you may have to edit the parameters before submitting.

Swagger

- http://localhost:9002/spring-demo/swagger-ui/#
- Instead of using Postman, you can also use Swagger.  However, you have to get a JWT Token
  via Postman before you can use Swagger.
- After getting a JWT Token, click on Authorize button to enter JWT Token. Inside Authorize, 
  enter "Bearer <JWT Token>".
- After logging in JWT Token, you can now try the end points via the Swagger GUI.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.0/maven-plugin/reference/html/#build-image)
