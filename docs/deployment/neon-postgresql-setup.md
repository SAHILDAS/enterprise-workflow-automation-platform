# Neon PostgreSQL Setup Guide

## Purpose

This document describes how the **Enterprise Workflow Automation Platform** is configured to use **Neon PostgreSQL** as its managed cloud database.

The platform uses a single Neon PostgreSQL database that is shared by both microservices:

* Workflow Service
* Execution Service

Database schema management is performed automatically through **Flyway migrations** during application startup.

No manual table creation is required.

---

# Architecture

```text
Local Development / Render
           |
           |
Spring Boot Microservices
           |
           |
     JDBC over SSL
           |
           |
     Neon PostgreSQL
```

Neon provides:

* Managed PostgreSQL hosting
* Automatic backups
* SSL encryption
* Public connectivity
* Connection pooling
* Free tier suitable for MVP deployment

---

# Prerequisites

* GitHub account
* Neon account
* Java 21
* Maven
* Spring Boot 3.5.x

---

# Create a Neon Project

1. Open:

   https://neon.tech

2. Sign in with GitHub.

3. Create a new project.

Recommended configuration:

| Setting            | Value                        |
| ------------------ | ---------------------------- |
| Project Name       | enterprise-workflow-platform |
| Region             | Closest available region     |
| PostgreSQL Version | Default                      |
| Database Name      | neondb (default)             |

---

# Obtain the JDBC Connection URL

In the Neon dashboard:

* Click **Connect**
* Select **JDBC**
* Disable connection pooling (for this project)

Example:

```text
jdbc:postgresql://ep-example.us-east-2.aws.neon.tech/neondb?sslmode=require
```

Required values:

* JDBC URL
* Database username
* Database password

---

# Environment Variables

The project is fully environment-variable driven.

## Workflow Service

PowerShell:

```powershell
$env:DATABASE_URL="jdbc:postgresql://YOUR_NEON_HOST/neondb?sslmode=require"
$env:DATABASE_USERNAME="YOUR_USERNAME"
$env:DATABASE_PASSWORD="YOUR_PASSWORD"
$env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
$env:SERVER_PORT="8081"
```

Run:

```bash
mvn -pl services/workflow-service spring-boot:run
```

---

## Execution Service

PowerShell:

```powershell
$env:DATABASE_URL="jdbc:postgresql://YOUR_NEON_HOST/neondb?sslmode=require"
$env:DATABASE_USERNAME="YOUR_USERNAME"
$env:DATABASE_PASSWORD="YOUR_PASSWORD"
$env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
$env:SERVER_PORT="8082"
```

Run:

```bash
mvn -pl services/execution-service spring-boot:run
```

---

# SSL Configuration

Neon requires SSL.

The JDBC URL **must include**:

```text
?sslmode=require
```

Example:

```text
jdbc:postgresql://ep-example.us-east-2.aws.neon.tech/neondb?sslmode=require
```

Without SSL, the connection may fail during startup.

---

# Spring Boot Configuration

The project uses environment variables in `application.yml`.

Example:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5433/workflow_db}
    username: ${DATABASE_USERNAME:workflow_user}
    password: ${DATABASE_PASSWORD:workflow_password}
```

This allows:

* Local Docker PostgreSQL
* Neon PostgreSQL
* Render deployment
* Future AWS deployment

without changing application code.

---

# Flyway Migration Verification

On first startup, Flyway automatically creates the schema.

Workflow Service creates:

* flyway_schema_history
* workflows
* workflow_versions
* workflow_steps

Execution Service creates:

* execution_flyway_schema_history
* workflow_executions
* step_executions

Expected startup log:

```text
Flyway Community Edition
Successfully validated migration
Migrating schema
Successfully applied migration
```

---

# Verify in Neon Dashboard

Open:

Neon Dashboard → Tables

Expected tables:

## Workflow Service

* workflows
* workflow_versions
* workflow_steps
* flyway_schema_history

## Execution Service

* workflow_executions
* step_executions
* execution_flyway_schema_history

---

# Test Database Connectivity

Health endpoint:

Workflow Service:

```text
http://localhost:8081/actuator/health
```

Execution Service:

```text
http://localhost:8082/actuator/health
```

Expected:

```json
{
  "status": "UP"
}
```

---

# Common Issues

## Invalid SSL configuration

Symptom:

```text
SSL connection required
```

Solution:

Ensure the JDBC URL contains:

```text
?sslmode=require
```

---

## Authentication failed

Symptom:

```text
FATAL: password authentication failed
```

Solution:

Verify:

* DATABASE_USERNAME
* DATABASE_PASSWORD

---

## Database not found

Symptom:

```text
database "workflow_db" does not exist
```

Solution:

Use the actual Neon database name (commonly `neondb`).

---

## Flyway validation error

Symptom:

```text
Validate failed
```

Solution:

Ensure both services use separate Flyway history tables.

Workflow Service:

```text
flyway_schema_history
```

Execution Service:

```text
execution_flyway_schema_history
```

---

# Local Docker vs Neon

| Environment       | Host                  | Port       |
| ----------------- | --------------------- | ---------- |
| Docker PostgreSQL | workflow-postgres     | 5432       |
| Host Machine      | localhost             | 5433       |
| Neon PostgreSQL   | ep-xxxx.aws.neon.tech | 5432 (SSL) |

---

# Security Notes

Never commit:

* DATABASE_URL
* DATABASE_USERNAME
* DATABASE_PASSWORD

Use:

* Render Environment Variables
* GitHub Secrets
* CI/CD secrets management

---

# Production Deployment

Render uses the same variables:

| Variable               | Example               |
| ---------------------- | --------------------- |
| SPRING_PROFILES_ACTIVE | prod                  |
| DATABASE_URL           | jdbc:postgresql://... |
| DATABASE_USERNAME      | neondb_owner          |
| DATABASE_PASSWORD      | ********              |

No code changes are required for deployment.

---

# Current Project Status

This project has been successfully validated against Neon PostgreSQL.

Verified:

* Workflow Service connection
* Execution Service connection
* SSL connectivity
* Flyway migrations
* Table creation
* Render deployment compatibility

The platform is now using **managed cloud PostgreSQL infrastructure** instead of local-only database storage.
