# Deployment Overview

## Purpose

This document describes the deployment architecture and deployment strategy for the **Enterprise Workflow Automation Platform**.

The platform is deployed using managed cloud infrastructure and containerized Spring Boot microservices.

The deployment process is designed to be reproducible, environment-variable driven, and suitable for future migration to AWS, Kubernetes, or other cloud platforms.

---

# Production Architecture

```text
                    GitHub Repository
                           |
                           |
                    Render Auto Deploy
                           |
        -----------------------------------------
        |                                       |
        |                                       |
Workflow Service                         Execution Service
(Render Docker)                          (Render Docker)
        |                                       |
        |                                       |
        ----------- Neon PostgreSQL -------------
                     (Managed Database)

Kafka integration is temporarily disabled in the production profile
until a managed Kafka provider is introduced.
```

---

# Deployment Components

## Workflow Service

* Java 21
* Spring Boot 3.5
* Docker
* Render Web Service
* Public REST API
* Swagger/OpenAPI
* Flyway migrations

Production URL:

```text
https://workflow-service-f5j3.onrender.com
```

Swagger:

```text
https://workflow-service-f5j3.onrender.com/swagger-ui
```

Health:

```text
https://workflow-service-f5j3.onrender.com/actuator/health
```

---

## Execution Service

* Java 21
* Spring Boot 3.5
* Docker
* Render Web Service
* Flyway migrations
* Kafka consumer disabled in production profile

Production URL:

```text
https://execution-service-v39f.onrender.com
```

Swagger:

```text
https://execution-service-v39f.onrender.com/swagger-ui
```

Health:

```text
https://execution-service-v39f.onrender.com/actuator/health
```

---

## Database

Provider:

**Neon PostgreSQL**

Characteristics:

* Managed PostgreSQL
* SSL enabled
* Automatic backups
* Public connectivity
* Environment-variable based configuration

The database schema is managed entirely through **Flyway migrations**.

No manual table creation is required during deployment.

---

# Deployment Strategy

The deployment process consists of four stages.

## Stage 1 — Local Containerization

* Build executable Spring Boot JARs
* Build Docker images
* Run PostgreSQL, Kafka, Redis, and Kafka UI locally
* Verify inter-service communication

Reference:

`docs/infrastructure/docker-containerization-guide.md`

---

## Stage 2 — Cloud Database

* Create Neon PostgreSQL instance
* Configure JDBC connection
* Run Flyway migrations
* Verify schema creation

Reference:

`docs/deployment/neon-postgresql-setup.md`

---

## Stage 3 — Workflow Service Deployment

* Create Render Web Service
* Configure Docker deployment
* Configure production environment variables
* Verify health endpoint
* Verify Swagger endpoint

Reference:

`docs/deployment/render-workflow-service-deployment.md`

---

## Stage 4 — Execution Service Deployment

* Create Render Web Service
* Configure Docker deployment
* Configure production environment variables
* Disable Kafka consumer in production profile
* Verify health endpoint
* Verify Swagger endpoint

Reference:

`docs/deployment/render-execution-service-deployment.md`

---

# Environment Configuration

Both services are fully environment-variable driven.

Key variables:

| Variable                | Description               |
| ----------------------- | ------------------------- |
| SPRING_PROFILES_ACTIVE  | Active Spring profile     |
| DATABASE_URL            | Neon JDBC URL             |
| DATABASE_USERNAME       | Database username         |
| DATABASE_PASSWORD       | Database password         |
| KAFKA_BOOTSTRAP_SERVERS | Kafka broker address      |
| PORT                    | Render-assigned HTTP port |

Reference:

`docs/deployment/environment-variables.md`

---

# Deployment Verification

A deployment is considered successful when:

* Render service status is **Live**
* Health endpoint returns **UP**
* Swagger UI is accessible
* Database connectivity is successful
* Flyway migrations complete successfully
* No startup exceptions appear in Render logs

Reference:

`docs/deployment/deployment-verification-checklist.md`

---

# Current Production Status

| Component               | Status    |
| ----------------------- | --------- |
| Workflow Service        | Deployed  |
| Execution Service       | Deployed  |
| Neon PostgreSQL         | Connected |
| Flyway Migrations       | Applied   |
| Docker Containerization | Completed |
| Render Deployment       | Completed |
| Kafka Cloud Deployment  | Pending   |

---

# Future Improvements

Planned enhancements:

* Managed Kafka deployment
* Redis cloud deployment
* GitHub Actions CI/CD
* Automatic Docker image publishing
* Blue/green deployment
* Kubernetes deployment manifests
* AWS ECS/Fargate deployment
* Observability (Prometheus/Grafana/OpenTelemetry)

The current deployment architecture intentionally prioritizes a **production-quality MVP** that can be deployed free of cost while remaining extensible for enterprise-scale infrastructure.


## Current Deployment

### Workflow Service

Base URL:
https://workflow-service-f5j3.onrender.com

Swagger UI:
https://workflow-service-f5j3.onrender.com/swagger-ui

Health:
https://workflow-service-f5j3.onrender.com/actuator/health

### Execution Service

Base URL:
https://execution-service-v39f.onrender.com

Swagger UI:
https://execution-service-v39f.onrender.com/swagger-ui

Health:
https://execution-service-v39f.onrender.com/actuator/health