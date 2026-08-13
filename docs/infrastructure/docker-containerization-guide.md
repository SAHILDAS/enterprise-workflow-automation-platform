# Docker Containerization Guide

## Purpose

This document explains how the Enterprise Workflow Automation Platform is containerized for local development, testing, and production deployment.

Both microservices use production-ready multi-stage Docker builds and run as non-root users. The same Docker images can be used for local testing, Render deployment, and future AWS/Kubernetes deployments.

---

# Architecture

The platform consists of two Spring Boot microservices:

* Workflow Service
* Execution Service

Both services connect to shared infrastructure components running in Docker:

* PostgreSQL
* Kafka
* Redis
* Kafka UI

The services communicate with PostgreSQL and Kafka through the Docker network.

---

# Dockerfile Locations

## Workflow Service

Path:

```text
services/workflow-service/Dockerfile
```

## Execution Service

Path:

```text
services/execution-service/Dockerfile
```

---

# Dockerfile Structure

Each Dockerfile uses a multi-stage build.

## Builder Stage

```dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS builder
```

Responsibilities:

* Uses Maven with JDK 21
* Copies the root pom.xml
* Copies the services directory
* Builds only the required module
* Produces an executable Spring Boot JAR

Example build command inside the Dockerfile:

```dockerfile
RUN mvn -pl services/workflow-service -am clean package -DskipTests
```

## Runtime Stage

```dockerfile
FROM eclipse-temurin:21-jre
```

Responsibilities:

* Uses a lightweight Java 21 runtime image
* Creates a non-root user named spring
* Copies the executable JAR from the builder stage
* Exposes the application port
* Starts the application with `java -jar`

Example:

```dockerfile
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
```

---

# Why Multi-Stage Builds?

Advantages:

* Smaller final image
* Faster deployment
* Better security
* No Maven installation in production image
* Reproducible builds

---

# Build Docker Images

All commands are executed from the repository root.

## Build Workflow Service

```bash
docker build -f services/workflow-service/Dockerfile -t workflow-service:local .
```

## Build Execution Service

```bash
docker build -f services/execution-service/Dockerfile -t execution-service:local .
```

---

# Verify Images

List available images:

```bash
docker images
```

Expected images:

* workflow-service:local
* execution-service:local

---

# Docker Network

Infrastructure containers are started using Docker Compose and are attached to the `docker_default` network.

Verify available networks:

```bash
docker network ls
```

The application containers must join the same network to communicate with PostgreSQL and Kafka.

---

# Run Workflow Service

```bash
docker run -d \
  --name workflow-service-test \
  --network docker_default \
  -p 8081:8081 \
  -e DATABASE_URL=jdbc:postgresql://workflow-postgres:5432/workflow_db \
  -e DATABASE_USERNAME=workflow_user \
  -e DATABASE_PASSWORD=workflow_password \
  -e KAFKA_BOOTSTRAP_SERVERS=workflow-kafka:9093 \
  workflow-service:local
```

---

# Run Execution Service

```bash
docker run -d \
  --name execution-service-test \
  --network docker_default \
  -p 8082:8082 \
  -e DATABASE_URL=jdbc:postgresql://workflow-postgres:5432/workflow_db \
  -e DATABASE_USERNAME=workflow_user \
  -e DATABASE_PASSWORD=workflow_password \
  -e KAFKA_BOOTSTRAP_SERVERS=workflow-kafka:9093 \
  execution-service:local
```

---

# Why 5432 Instead of 5433?

Host machine:

```text
localhost:5433
```

Inside Docker network:

```text
workflow-postgres:5432
```

Docker containers communicate using the container’s internal port.

---

# Verify Running Containers

List running containers:

```bash
docker ps
```

List all containers (including stopped):

```bash
docker ps -a
```

---

# View Container Logs

## Workflow Service

```bash
docker logs workflow-service-test
```

Follow logs continuously:

```bash
docker logs -f workflow-service-test
```

## Execution Service

```bash
docker logs execution-service-test
```

Follow logs continuously:

```bash
docker logs -f execution-service-test
```

---

# Verify Application Health

## Workflow Service

Health endpoint:

```text
http://localhost:8081/actuator/health
```

Swagger UI:

```text
http://localhost:8081/swagger-ui
```

## Execution Service

Health endpoint:

```text
http://localhost:8082/actuator/health
```

Swagger UI:

```text
http://localhost:8082/swagger-ui
```

Expected health response:

```json
{
  "status": "UP"
}
```

---

# Verify Kafka Connectivity

Activate a workflow:

```bash
curl -X POST http://localhost:8081/api/v1/workflows/{workflowId}/activate
```

Then inspect execution service logs:

```bash
docker logs -f execution-service-test
```

Expected result:

* WorkflowActivatedEvent received
* Kafka consumer processed the event successfully

---

# Stop Containers

Stop a specific container:

```bash
docker stop workflow-service-test
```

Stop multiple containers:

```bash
docker stop workflow-service-test execution-service-test
```

---

# Remove Containers

Remove stopped containers:

```bash
docker rm workflow-service-test
```

Remove multiple containers:

```bash
docker rm workflow-service-test execution-service-test
```

Force remove running containers:

```bash
docker rm -f workflow-service-test execution-service-test
```

---

# Remove Docker Images

Remove a specific image:

```bash
docker rmi workflow-service:local
```

Remove both images:

```bash
docker rmi workflow-service:local execution-service:local
```

Force removal:

```bash
docker rmi -f workflow-service:local execution-service:local
```

---

# Complete Cleanup

Stop and remove containers:

```bash
docker rm -f workflow-service-test execution-service-test
```

Remove images:

```bash
docker rmi workflow-service:local execution-service:local
```

Prune unused Docker resources:

```bash
docker system prune -f
```

---

# Development Workflow

Typical developer workflow:

Build services

```bash
mvn clean package -DskipTests
```

Build Docker images

```bash
docker build -f services/workflow-service/Dockerfile -t workflow-service:local .
docker build -f services/execution-service/Dockerfile -t execution-service:local .
```

Run containers

```bash
docker run ...
```

Verify

```bash
docker ps
docker logs workflow-service-test
docker logs execution-service-test
```

Test APIs

```text
http://localhost:8081/swagger-ui
http://localhost:8082/swagger-ui
```

Clean up

```bash
docker rm -f workflow-service-test execution-service-test
```

---

# Production Readiness Notes

The Docker images are designed to be compatible with:

* Render
* AWS ECS / Fargate
* Kubernetes
* Docker Compose
* Any OCI-compliant container runtime

Key production features:

* Multi-stage builds
* Java 21 runtime
* Non-root execution
* Environment-variable configuration
* Container-aware JVM settings
* Small runtime image
* Executable Spring Boot fat JAR packaging

This containerization approach provides a consistent runtime environment across local development, cloud deployment, and future production infrastructure.
