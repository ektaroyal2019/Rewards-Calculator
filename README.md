# Rewards calculator Application

This application has APIs for basic CURD transaction operations, and calculate reward points for the client.

# Quick start

### Prerequisites
* [Install Maven](https://maven.apache.org/install.html)
* [Install Java 17](https://docs.oracle.com/en/java/javase/18/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A)

### General information
Application is backed by H2 in-memory database so all user data are retained as long as application is running.
You can easily replace the database with any other relational database by changing the appropriate entries in application.properties file

#### Application run
To run the application please fallow below steps:
______
* clone git repository
```shell script
git clone {gitProjetcURL}
```

* Build the application:
```shell script
mvn clean install
```

* Run application locally
```shell script
java -jar .\target\Rewards-Calculator-0.0.1-SNAPSHOT.jar
```

Then APIs can be accessed on port 8081.

### Test run guide

`mvn test ` run tests

### Postman collections
To use the Rewards Calculation service you can use Postman collections located in **postmanCollection** directory