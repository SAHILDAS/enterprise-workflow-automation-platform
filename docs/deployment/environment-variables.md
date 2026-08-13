# Environment Variables Reference

## Purpose

This document defines all environment variables used by the Enterprise Workflow Automation Platform.

The project follows the Twelve-Factor App methodology by externalizing environment-specific configuration from application code.

Environment variables are used to configure:

* Database connections
* Kafka connectivity
* Spring profiles
* Application ports
* Deployment environments

This approach enables the same Docker image to run consistently across:

* Local development
* Docker environments
* Render deployments
* Future cloud platforms

---

# Environment Overview

| Variable                | Workflow Service | Execution Service | Local    | Render         | Required |
| ----------------------- | ---------------- | ----------------- | -------- | -------------- | -------- |
| SPRING_PROFILES_ACTIVE  | Yes              | Yes               | Optional | Yes            | Yes      |
| DATABASE_URL            | Yes              | Yes               | Optional | Yes            | Yes      |
| DATABASE_USERNAME       | Yes              | Yes               | Optional | Yes            | Yes      |
| DATABASE_PASSWORD       | Yes              | Yes               | Optional | Yes            | Yes      |
| KAFKA_BOOTSTRAP_SERVERS | Yes              | Yes               | Yes      | Optional       | No       |
| PORT                    | Yes              | Yes               | Optional | Render Managed | No       |

---

# Spring Profiles

## SPRING_PROFILES_ACTIVE

Determines which Spring Boot profile is loaded during application startup.

### Example

```bash
SPRING_PROFILES_ACTIVE=prod
```

### Supported Profiles

| Profile | Purpose               |
| ------- | --------------------- |
| default | Local development     |
| prod    | Production deployment |

### Local Behavior

If not specified:

```yaml
application.yml
```

is loaded automatically.

### Production Behavior

If set to:

```bash
SPRING_PROFILES_ACTIVE=prod
```

Spring Boot loads:

```yaml
application-prod.yml
```

---

# Database Configuration

## DATABASE_URL

JDBC connection string used by PostgreSQL.

### Local Docker Example

```bash
jdbc:postgresql://localhost:5433/workflow_db
```

### Neon Example

```bash
jdbc:postgresql://ep-example.us-east-1.aws.neon.tech/neondb?sslmode=require
```

### Usage

Workflow Service:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
```

Execution Service:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
```

---

## DATABASE_USERNAME

Database username.

### Example

```bash
DATABASE_USERNAME=workflow_owner
```

### Usage

```yaml
spring:
  datasource:
    username: ${DATABASE_USERNAME}
```

---

## DATABASE_PASSWORD

Database password.

### Example

```bash
DATABASE_PASSWORD=********
```

### Usage

```yaml
spring:
  datasource:
    password: ${DATABASE_PASSWORD}
```

---

# Kafka Configuration

## KAFKA_BOOTSTRAP_SERVERS

Kafka bootstrap server list used for producer and consumer communication.

### Local Docker Example

```bash
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

### Docker Network Example

```bash
KAFKA_BOOTSTRAP_SERVERS=workflow-kafka:9093
```

### Future Managed Kafka Example

```bash
KAFKA_BOOTSTRAP_SERVERS=broker1:9092,broker2:9092
```

### Workflow Service Usage

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS}
```

### Execution Service Usage

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS}
```

---

# Application Port

## PORT

Defines the HTTP server port.

### Local Example

```bash
PORT=8081
```

### Render Example

Render automatically injects this value.

No manual configuration is required.

### Usage

```yaml
server:
  port: ${PORT:8081}
```

The application defaults to:

```text
8081
```

when no PORT variable is provided.

---

# Workflow Service Environment Variables

## Render Configuration

```env
SPRING_PROFILES_ACTIVE=prod

DATABASE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
DATABASE_USERNAME=<username>
DATABASE_PASSWORD=<password>

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

---

# Execution Service Environment Variables

## Render Configuration

```env
SPRING_PROFILES_ACTIVE=prod

DATABASE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
DATABASE_USERNAME=<username>
DATABASE_PASSWORD=<password>

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

---

# Local Development Variables

Workflow Service

```env
DATABASE_URL=jdbc:postgresql://localhost:5433/workflow_db
DATABASE_USERNAME=workflow_user
DATABASE_PASSWORD=workflow_password

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

Execution Service

```env
DATABASE_URL=jdbc:postgresql://localhost:5433/workflow_db
DATABASE_USERNAME=workflow_user
DATABASE_PASSWORD=workflow_password

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

---

# Production Kafka Strategy

Current production deployment does not use a managed Kafka provider.

To allow deployment on Render Free Tier:

* Workflow Service publishes events conditionally.
* Execution Service disables Kafka listeners in the production profile.
* Core REST functionality remains available.

Future phases will introduce:

* Confluent Cloud
* Upstash Kafka
* Redpanda Cloud
* Amazon MSK

without requiring application code changes.

---

# Security Guidelines

Never commit:

```env
DATABASE_PASSWORD
```

or any production secrets to Git.

Recommended locations:

* Render Environment Variables
* GitHub Secrets
* Future Secret Manager integrations

Examples:

* AWS Secrets Manager
* HashiCorp Vault
* Doppler
* 1Password Secrets Automation

---

# Verification

## Verify Active Profile

```bash
echo $SPRING_PROFILES_ACTIVE
```

or inspect startup logs:

```text
The following 1 profile is active: "prod"
```

---

## Verify Database Connectivity

```bash
/actuator/health
```

Expected:

```json
{
  "status": "UP"
}
```

---

## Verify Deployment

Workflow Service:

```text
https://workflow-service-f5j3.onrender.com
```

Execution Service:

```text
https://execution-service-v39f.onrender.com
```

Successful startup confirms:

* Environment variables loaded
* Neon connectivity established
* Flyway migrations executed
* Spring Boot application started
