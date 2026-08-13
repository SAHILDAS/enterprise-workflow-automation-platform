# Render Deployment Guide — Execution Service

## Purpose

This document describes how the **Execution Service** of the **Enterprise Workflow Automation Platform** is deployed to **Render** using Docker and a managed **Neon PostgreSQL** database.

The Execution Service normally consumes Kafka events from the Workflow Service. For the MVP deployment, Kafka consumption is intentionally disabled in the production profile so that the service can be deployed independently without a managed Kafka provider.

This guide documents the exact deployment process used for the production deployment.

---

# Deployment Architecture

```text
GitHub Repository
        |
        |
Render Docker Build
        |
        |
Execution Service (Render)
        |
        |
Neon PostgreSQL
```

The Execution Service is deployed independently and communicates directly with the shared Neon PostgreSQL database.

Kafka consumption is disabled in production and remains enabled for local development.

---

# Prerequisites

* GitHub repository
* Render account
* Neon PostgreSQL database
* Dockerfile committed to the repository
* Production configuration (`application-prod.yml`)

---

# Repository

Repository:

```text
https://github.com/SAHILDAS/enterprise-workflow-automation-platform
```

Branch:

```text
main
```

---

# Dockerfile

Path:

```text
services/execution-service/Dockerfile
```

The Dockerfile performs a multi-stage build.

Builder image:

```text
maven:3.9.9-eclipse-temurin-21
```

Runtime image:

```text
eclipse-temurin:21-jre
```

The final image contains only:

* Java 21 runtime
* Executable Spring Boot fat JAR
* Non-root application user

---

# Create Render Web Service

Render Dashboard:

* New
* Web Service
* Connect GitHub repository

Select:

```text
enterprise-workflow-automation-platform
```

---

# Service Configuration

| Setting         | Value                                 |
| --------------- | ------------------------------------- |
| Name            | execution-service                     |
| Runtime         | Docker                                |
| Branch          | main                                  |
| Dockerfile Path | services/execution-service/Dockerfile |
| Root Directory  | (leave empty)                         |
| Region          | same region as Workflow Service       |
| Plan            | Free                                  |

---

# Environment Variables

The service uses the **production Spring profile**.

Add the following environment variables.

## Required

| Variable                | Description            |
| ----------------------- | ---------------------- |
| SPRING_PROFILES_ACTIVE  | Active Spring profile  |
| DATABASE_URL            | Neon JDBC URL          |
| DATABASE_USERNAME       | Neon username          |
| DATABASE_PASSWORD       | Neon password          |
| KAFKA_BOOTSTRAP_SERVERS | Kafka bootstrap server |

Example:

```text
SPRING_PROFILES_ACTIVE=prod

DATABASE_URL=jdbc:postgresql://ep-example.aws.neon.tech/neondb?sslmode=require

DATABASE_USERNAME=neondb_owner

DATABASE_PASSWORD=********

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

Render automatically provides:

```text
PORT
```

No manual configuration is required.

---

# Production Configuration

The service uses:

```text
application-prod.yml
```

Important production settings:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}

server:
  port: ${PORT:8082}
```

Kafka health checks are disabled:

```yaml
management:
  health:
    kafka:
      enabled: false
```

---

# Kafka Consumer Strategy

The Execution Service normally consumes workflow lifecycle events using a Spring Kafka listener.

For production deployment, the consumer bean is conditionally disabled using:

```java
@ConditionalOnProperty(
    name = "workflow.kafka.consumer.enabled",
    havingValue = "true",
    matchIfMissing = true
)
```

Production profile:

```yaml
workflow:
  kafka:
    consumer:
      enabled: false
```

Result:

* Local development: Kafka consumer enabled
* Render production: Kafka consumer disabled
* Future managed Kafka deployment: enable with an environment property

This allows the service to remain operational even when Kafka infrastructure is not yet available in the cloud.

---

# Deployment Process

After clicking **Create Web Service**, Render performs:

1. Clone repository
2. Build Docker image
3. Run container
4. Inject environment variables
5. Start Spring Boot
6. Connect to Neon
7. Execute Flyway migrations
8. Expose public HTTPS endpoint

---

# Successful Deployment Logs

Expected startup sequence:

```text
Started ExecutionServiceApplication
Tomcat started on port 10000
Your service is live
```

The deployment is considered successful when Render reports:

```text
Your service is live
```

---

# Production URL

Execution Service:

```text
https://execution-service-v39f.onrender.com
```

---

# Verification

## Health Endpoint

```text
https://execution-service-v39f.onrender.com/actuator/health
```

Expected:

```json
{
  "status": "UP"
}
```

---

## Swagger UI

```text
https://execution-service-v39f.onrender.com/swagger-ui
```

---

## OpenAPI Documentation

```text
https://execution-service-v39f.onrender.com/api-docs
```

---

# Database Verification

After deployment, verify in Neon Dashboard:

Tables:

* workflow_executions
* step_executions
* execution_flyway_schema_history

This confirms that Flyway executed successfully in the Render environment.

---

# Common Issues

## Dockerfile not found

Symptom:

```text
Dockerfile does not exist
```

Solution:

Ensure:

```text
services/execution-service/Dockerfile
```

is specified correctly.

---

## Database authentication failure

Symptom:

```text
FATAL: password authentication failed
```

Solution:

Verify:

* DATABASE_USERNAME
* DATABASE_PASSWORD

---

## SSL connection failure

Symptom:

```text
SSL connection required
```

Solution:

Ensure:

```text
?sslmode=require
```

is present in the JDBC URL.

---

## Kafka unavailable

Expected behavior in production:

* Kafka consumer is disabled
* application starts successfully
* database operations continue normally
* service remains healthy

No action is required for the MVP deployment.

---

# Redeployment

Any push to the `main` branch can trigger a new Render deployment.

Manual redeployment:

Render Dashboard:

* Service
* Manual Deploy
* Deploy latest commit

---

# Production Status

Current deployment:

| Component         | Status                 |
| ----------------- | ---------------------- |
| Execution Service | Deployed               |
| Render            | Operational            |
| Neon PostgreSQL   | Connected              |
| Flyway            | Successful             |
| Swagger           | Public                 |
| Kafka Consumer    | Disabled in production |

---

# Future Improvements

Planned enhancements:

* Managed Kafka provider
* Automatic event consumption in production
* Redis cloud deployment
* GitHub Actions automated deployment
* Docker image registry
* Zero-downtime deployment strategy
* Kubernetes deployment manifests
* AWS ECS/Fargate deployment

Once a managed Kafka service is introduced, the Execution Service can begin consuming workflow lifecycle events in production without any architectural changes. Only configuration changes will be required.


## Current Deployment

### Execution Service

Base URL:
https://execution-service-v39f.onrender.com

Swagger UI:
https://execution-service-v39f.onrender.com/swagger-ui

Health:
https://execution-service-v39f.onrender.com/actuator/health