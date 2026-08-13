# Deployment Verification Checklist

## Purpose

This document provides a standardized operational checklist for verifying deployments of the **Enterprise Workflow Automation Platform**.

It should be executed after:

* Render deployments
* Manual redeployments
* Infrastructure changes
* Database migrations
* Environment variable updates
* Docker image updates

The objective is to ensure that all deployed components are healthy, accessible, and connected correctly before a deployment is considered successful.

---

# Production Environment

## Workflow Service

URL:

```text
https://workflow-service-f5j3.onrender.com
```

## Execution Service

URL:

```text
https://execution-service-v39f.onrender.com
```

## Database

Provider:

**Neon PostgreSQL**

---

# Deployment Status Checklist

| Component         | Expected Status   | Verified |
| ----------------- | ----------------- | -------- |
| Workflow Service  | Live              | ☐        |
| Execution Service | Live              | ☐        |
| Neon PostgreSQL   | Connected         | ☐        |
| Flyway Migrations | Applied           | ☐        |
| Swagger UI        | Accessible        | ☐        |
| Health Endpoints  | UP                | ☐        |
| API Documentation | Available         | ☐        |
| Database Tables   | Present           | ☐        |
| Render Logs       | No startup errors | ☐        |

---

# Workflow Service Verification

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

Verification:

* HTTP 200
* Status = UP

Result:

☐ Pass

---

## Swagger UI

```text
https://workflow-service-f5j3.onrender.com/swagger-ui
```

Verification:

* UI loads successfully
* Endpoints are visible
* API operations are callable

Result:

☐ Pass

---

## OpenAPI Documentation

```text
https://workflow-service-f5j3.onrender.com/api-docs
```

Verification:

* JSON document loads
* Schema is valid
* Endpoint list is complete

Result:

☐ Pass

---

## API Verification

Example:

Create Workflow

```http
POST /api/v1/workflows
```

Verification:

* HTTP 201 / 200
* Workflow persisted in database
* Response contains workflow identifier

Result:

☐ Pass

---

# Execution Service Verification

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

Result:

☐ Pass

---

## Swagger UI

```text
https://execution-service-v39f.onrender.com/swagger-ui
```

Verification:

* UI loads
* Endpoints are visible
* Request execution succeeds

Result:

☐ Pass

---

## OpenAPI Documentation

```text
https://execution-service-v39f.onrender.com/api-docs
```

Result:

☐ Pass

---

## API Verification

Example:

Create Execution

```http
POST /api/v1/executions
```

Verification:

* Execution record created
* Database updated
* Response returned successfully

Result:

☐ Pass

---

# Database Verification

## Neon Dashboard

Open:

Neon Dashboard → Tables

Expected tables:

### Workflow Service

* workflows
* workflow_versions
* workflow_steps
* flyway_schema_history

### Execution Service

* workflow_executions
* step_executions
* execution_flyway_schema_history

Verification:

* Tables exist
* Row counts are accessible
* No migration errors visible

Result:

☐ Pass

---

# Flyway Verification

Render startup logs should contain messages similar to:

```text
Successfully validated migration
```

and

```text
Successfully applied migration
```

Verification:

* No validation failures
* No checksum mismatches
* No migration exceptions

Result:

☐ Pass

---

# Render Log Verification

Workflow Service:

Render Dashboard → workflow-service → Logs

Execution Service:

Render Dashboard → execution-service → Logs

Verify:

* Application started successfully
* No repeated exception loops
* No database authentication errors
* No port binding failures
* No Flyway failures

Expected startup message:

```text
Started WorkflowServiceApplication
```

and

```text
Started ExecutionServiceApplication
```

Result:

☐ Pass

---

# Kafka Verification

## Local Development

Ensure Kafka container is running:

```bash
docker ps
```

Expected:

* workflow-kafka
* workflow-kafka-ui

---

## Workflow Event Test

Activate a workflow:

```http
POST /api/v1/workflows/{id}/activate
```

Verify:

* Workflow activation succeeds
* Event publication logs appear
* Execution service receives event (local environment)

Result:

☐ Pass

---

# Container Verification

## Running Containers

```bash
docker ps
```

Expected local containers:

* workflow-postgres
* workflow-kafka
* workflow-kafka-ui
* workflow-redis

Result:

☐ Pass

---

# Environment Variable Verification

Workflow Service:

* SPRING_PROFILES_ACTIVE
* DATABASE_URL
* DATABASE_USERNAME
* DATABASE_PASSWORD
* KAFKA_BOOTSTRAP_SERVERS

Execution Service:

* SPRING_PROFILES_ACTIVE
* DATABASE_URL
* DATABASE_USERNAME
* DATABASE_PASSWORD
* KAFKA_BOOTSTRAP_SERVERS

Verification:

* Variables present
* No empty values
* Correct Neon host
* Correct credentials

Result:

☐ Pass

---

# Common Failure Symptoms

## Database Authentication Failure

Symptom:

```text
FATAL: password authentication failed
```

Action:

* Verify DATABASE_USERNAME
* Verify DATABASE_PASSWORD

---

## SSL Failure

Symptom:

```text
SSL connection required
```

Action:

Ensure JDBC URL contains:

```text
?sslmode=require
```

---

## Flyway Validation Failure

Symptom:

```text
Validate failed
```

Action:

* Verify migration history
* Verify execution_flyway_schema_history
* Verify flyway_schema_history

---

## Port Binding Failure

Symptom:

```text
Port already in use
```

Action:

* Stop conflicting local process
* Verify Render PORT configuration

---

## Kafka Connection Failure

Expected behavior:

Workflow Service:

* Warning logged
* Application remains healthy

Execution Service:

* Kafka consumer disabled in production

No action required for the MVP deployment.

---

# Rollback Checklist

If deployment fails:

* Previous Render deployment available
* Previous Git commit identified
* Environment variables unchanged
* Database schema compatible
* Flyway version verified

Rollback steps:

1. Open Render Dashboard
2. Select service
3. Manual Deploy
4. Deploy previous successful commit

Result:

☐ Pass

---

# Release Acceptance Criteria

A deployment is considered production-ready when:

* Both Render services are Live
* Health endpoints return UP
* Swagger is accessible
* API endpoints function correctly
* Neon connectivity is confirmed
* Flyway migrations succeed
* No startup exceptions remain
* Logs show stable operation for 5+ minutes

---

# Verification Record

Deployment Date:

---

Git Commit:

---

Workflow Service Version:

---

Execution Service Version:

---

Verified By:

---

Notes:

---

---

---

---

# Current Project Status

This verification process has been successfully executed for the current deployment.

Verified:

* Workflow Service deployed
* Execution Service deployed
* Neon PostgreSQL connected
* Flyway migrations applied
* Public Swagger endpoints available
* Health endpoints operational
* Docker deployment validated
* Render deployment validated

This checklist should be executed after every production deployment and before marking a release as complete.


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