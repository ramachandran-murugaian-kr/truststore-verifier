# FutureEx Connection Test - Spring Boot Project

This project is a Spring Boot application designed to test the connection to the FutureEx external service.

## Prerequisites

- Java 21
- Spring Boot 3.x
- Gradle

## Environment Variables

Ensure the following environment variable is set before running the application:

```bash
export FUTURE_EX_KEY_STORE_PWD=<your-keystore-password>
```

## Configuration

Application-specific properties can be configured in the `application.yaml` file under
`src/main/resources`. Update the FutureEx service URL and other parameters as needed.

Example:

```yaml
future-ex:
  ssl-config:
    key-store-password: ${FUTURE_EX_KEY_STORE_PWD}
    key-store-path: truststore.p12
  url: https://kroger.uscentral1-cryptohub-uat.virtucrypt.com:10001
```

## How to Build and Run


### Using Gradle

```bash
./gradlew clean bootRun
```

## Running Tests

Unit and integration tests can be run using:

### Gradle

```bash
./gradlew test
```
