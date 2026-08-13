# Render Deployment Guide — Workflow Service

## Purpose

This document describes how the **Workflow Service** of the **Enterprise Workflow Automation Platform** is deployed to **Render** using Docker and a managed **Neon PostgreSQL** database.

The deployment is based on the production profile (`application-prod.yml`) and environment-variable configuration.

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
Workflow Service (Render)
        |
        |
Neon PostgreSQL
```

The Workflow Service is deployed independently and communicates directly with the shared Neon PostgreSQL database.

Kafka is temporarily treated as an optional dependency in the production environment.

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
services/workflow-service/Dockerfile
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

| Setting         | Value                                |
| --------------- | ------------------------------------ |
| Name            | workflow-service                     |
| Runtime         | Docker                               |
| Branch          | main                                 |
| Dockerfile Path | services/workflow-service/Dockerfile |
| Root Directory  | (leave empty)                        |
| Region          | nearest available                    |
| Plan            | Free                                 |

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
  port: ${PORT:8081}
```

Kafka health checks are disabled:

```yaml
management:
  health:
    kafka:
      enabled: false
```

This allows deployment even when a managed Kafka service is not yet configured.

---

# Kafka Behavior in Production

The Workflow Service publishes workflow lifecycle events.

In production, Kafka publishing is intentionally **non-blocking**.

If Kafka is unavailable:

* the API request succeeds
* the workflow is activated
* the application remains healthy
* a warning is written to the logs

This design allows the service to remain operational while cloud Kafka infrastructure is pending.

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
Started WorkflowServiceApplication
Tomcat started on port 10000
Your service is live
```

The deployment is considered successful when Render reports:

```text
Your service is live
```

---

# Production URL

Workflow Service:

```text
https://workflow-service-f5j3.onrender.com
```

---

# Verification

## Health Endpoint

```text
https://workflow-service-f5j3.onrender.com/actuator/health
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
https://workflow-service-f5j3.onrender.com/swagger-ui
```

---

## OpenAPI Documentation

```text
https://workflow-service-f5j3.onrender.com/api-docs
```

---

# Database Verification

After deployment, verify in Neon Dashboard:

Tables:

* workflows
* workflow_versions
* workflow_steps
* flyway_schema_history

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
services/workflow-service/Dockerfile
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

Symptom:

```text
Connection to node localhost:9092 could not be established
```

Expected behavior:

* application remains running
* workflow operations continue
* warning appears in logs

No action required for the MVP deployment.

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

| Component        | Status                              |
| ---------------- | ----------------------------------- |
| Workflow Service | Deployed                            |
| Render           | Operational                         |
| Neon PostgreSQL  | Connected                           |
| Flyway           | Successful                          |
| Swagger          | Public                              |
| Kafka            | Optional / pending cloud deployment |

---

# Future Improvements

Planned enhancements:

* Managed Kafka provider
* Redis cloud deployment
* GitHub Actions automated deployment
* Docker image registry
* Zero-downtime deployment strategy
* Kubernetes deployment manifests
* AWS ECS/Fargate deployment

The current Render deployment provides a fully functional public REST API backed by a managed cloud PostgreSQL database and serves as the production MVP deployment for the Enterprise Workflow Automation Platform.


## Current Deployment

### Workflow Service

Base URL:
https://workflow-service-f5j3.onrender.com

Swagger UI:
https://workflow-service-f5j3.onrender.com/swagger-ui

Health:
https://workflow-service-f5j3.onrender.com/actuator/health

