## FlyWay Migration

1. To add a migration script, create a new sql file in this directory.

2. The file should be named in this pattern: 'V{version}__{migration name}.sql'

	{version} should follow sequentially 1_0_1, 1_0_2... 1_0_11
	**NOTE: Even though 1_0_11 would follow 1_0_1 alpha numerically, FlyWay will still interpret it numerically
	**e.g. Flyway will process in this order: 1_0_1, 1_0_2, 1_0_11, NOT: 1_0_1, 1_0_11, 1_0_2

3. Upon installing and running the main application, FlyWay will initially create a table called flyway_schema_history to keep a history of migrations run.
FlyWay will look for new migration .sql scripts located in this directory each time the application is run.
Each new sql file it finds, Flyway will run the script and mark it as executed in the flyway_schema_history table
Scripts contained in this directory should be written such that they can be run repeatedly in a safe manner (e.g. use EXISTS checks for CREATE, INSERT, etc)

## How to get DDL SQL generated from Hibernate via Eclipse (Spring Tool Suite)

1. In storefrontservice project go to logback-spring.xml file and make sure 'org.hibernate.SQL' is set to 'DEBUG'.

2. Run application by right clicking on StorefrontServiceApplication.java => Run As => Java Application. 

3. In Console there will be SQL log statements displayed. 

Example:

13:16:14.148 [main] DEBUG org.hibernate.SQL - alter table client add column allow_ecommerce INTEGER NOT NULL
13:16:14.231 [main] DEBUG org.hibernate.SQL - alter table permission add column time_zone_code SMALLINT

4. Using the SQL Hibernate generated in the console, you can use it to create a DDL SQL file to be used in Flyway.
