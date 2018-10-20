# UMP-BACKEND
### Business Layer for UMP Application
#### Overview
* Provides additional business logic to Core ACS (GenieACS)
* Manages data model for each device type
* Exposes webservice(REST API) for front-end channels (__ump-webapp__) to manage remote CPE via Core ACS

#### How to start?
* Pre-requisite: Java, MySql,Redis, Maven
* Create database named __ump__ in MySQL
* Config connection information in _src/main/resources/liquibase/liquibase.properties_
* Run this command to init the database: 
```
mvn liquibase:update
```

* Start application:
```
mvn spring-boot:run
```
The application runs at port 8090 by default.