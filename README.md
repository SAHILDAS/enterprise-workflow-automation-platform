# Enterprise Workflow Automation Platform

A **production-grade event-driven microservices platform** built with **Java 21, Spring Boot 3.5, Apache Kafka, PostgreSQL, Docker, GitHub Actions, Neon PostgreSQL, and Render**.

This project demonstrates **enterprise backend engineering practices**, including **microservices architecture, asynchronous event processing, database migrations, cloud deployment, containerization, CI automation, API documentation, and production configuration management**.

---

## Architecture Overview

```text
                        +---------------------------+
                        |        Client / UI        |
                        +-------------+-------------+
                                      |
                                      v
                     +-------------------------------+
                     |       Workflow Service        |
                     |-------------------------------|
                     | Spring Boot 3.5               |
                     | Java 21                       |
                     | REST APIs                     |
                     | PostgreSQL (Neon)             |
                     | Flyway                        |
                     | Kafka Producer                |
                     | Swagger / Actuator            |
                     +---------------+---------------+
                                     |
                                     | workflow.lifecycle.events
                                     v
                         +---------------------------+
                         |       Apache Kafka        |
                         +-------------+-------------+
                                       |
                                       v
                     +-------------------------------+
                     |      Execution Service        |
                     |-------------------------------|
                     | Spring Boot 3.5               |
                     | Java 21                       |
                     | Kafka Consumer                |
                     | PostgreSQL (Neon)             |
                     | Flyway                        |
                     | Swagger / Actuator            |
                     +-------------------------------+
```

---

## Event Flow

```text
Create Workflow
       |
       v
Workflow Service
       |
       | Publish WorkflowCreatedEvent
       v
Apache Kafka
       |
       v
Execution Service
       |
       | Persist execution metadata
       v
PostgreSQL
```

The platform follows an **event-driven architecture**, where workflow lifecycle changes are published to Kafka and asynchronously consumed by downstream services.

---

## Technology Stack

### Backend

* Java 21
* Spring Boot 3.5.x
* Spring Web
* Spring Data JPA
* Spring Kafka
* Spring Actuator
* Swagger / OpenAPI

### Database

* PostgreSQL
* Neon PostgreSQL (cloud-hosted)
* Flyway database migrations

### Messaging

* Apache Kafka
* Event-driven asynchronous communication

### DevOps

* Docker
* Docker Compose
* GitHub Actions CI
* Render cloud deployment

### Documentation

* OpenAPI / Swagger
* Architecture documentation
* Deployment documentation
* Infrastructure documentation

---

## Microservices

### Workflow Service

Responsible for workflow management and event publishing.

Features:

* Create workflow
* List workflows
* Update workflow
* Activate / deactivate workflow
* Publish workflow lifecycle events

### Execution Service

Responsible for execution tracking and event consumption.

Features:

* Store workflow execution records
* Store step execution records
* Consume workflow lifecycle events
* Persist execution metadata

---

## Repository Structure

```text
enterprise-workflow-automation-platform/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── services/
│   ├── workflow-service/
│   │   ├── src/
│   │   ├── Dockerfile
│   │   └── pom.xml
│   │
│   └── execution-service/
│       ├── src/
│       ├── Dockerfile
│       └── pom.xml
│
├── docs/
│   ├── architecture/
│   ├── deployment/
│   └── infrastructure/
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Local Development Setup

### Prerequisites

* Java 21
* Maven 3.9+
* Docker Desktop
* Docker Compose
* Git

### Clone Repository

```bash
git clone https://github.com/SAHILDAS/enterprise-workflow-automation-platform.git

cd enterprise-workflow-automation-platform
```

### Start Infrastructure

```bash
docker compose up -d
```

This starts:

* PostgreSQL
* Apache Kafka
* Zookeeper

### Run Workflow Service

```bash
cd services/workflow-service

mvn spring-boot:run
```

Runs on:

```text
http://localhost:8081
```

### Run Execution Service

```bash
cd services/execution-service

mvn spring-boot:run
```

Runs on:

```text
http://localhost:8082
```

---

## Docker Setup

### Build Workflow Service

```bash
docker build -t workflow-service:local services/workflow-service
```

### Build Execution Service

```bash
docker build -t execution-service:local services/execution-service
```

### Run Workflow Service

```bash
docker run -p 8081:8081 workflow-service:local
```

### Run Execution Service

```bash
docker run -p 8082:8082 execution-service:local
```

---

## Cloud Database

The platform uses **Neon PostgreSQL** in production.

### Production Configuration

```properties
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
```

SSL is enabled using:

```text
?sslmode=require
```

Flyway migrations execute automatically on startup.

---

## Production Deployment

Both services are deployed publicly on **Render**.

### Workflow Service

Application:

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

### Execution Service

Application:

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

## Deployment Architecture

```text
             Internet
                 |
                 |
      +-----------------------+
      |        Render         |
      +----------+------------+
                 |
       +---------+---------+
       |                   |
       |                   |
+------+-----+     +-------+------+
| Workflow    |     | Execution    |
| Service     |     | Service      |
+------+-----+     +-------+------+
       |                   |
       +---------+---------+
                 |
                 |
         +-------+-------+
         | Neon PostgreSQL|
         +---------------+
```

Kafka is **temporarily disabled in production** so both services remain operational without a Kafka broker.

---

## Continuous Integration

GitHub Actions automatically validates every push and pull request.

Pipeline includes:

* Checkout repository
* Java 21 setup
* Maven dependency caching
* Root project build
* Workflow Service build
* Execution Service build
* JAR artifact upload
* Build failure detection

Workflow file:

```text
.github/workflows/ci.yml
```

---

## API Documentation

### Workflow Endpoints

| Method | Endpoint                       | Description         |
| ------ | ------------------------------ | ------------------- |
| POST   | /api/workflows                 | Create workflow     |
| GET    | /api/workflows                 | List workflows      |
| GET    | /api/workflows/{id}            | Get workflow        |
| PUT    | /api/workflows/{id}            | Update workflow     |
| PATCH  | /api/workflows/{id}/activate   | Activate workflow   |
| PATCH  | /api/workflows/{id}/deactivate | Deactivate workflow |

### Execution Endpoints

| Method | Endpoint                              | Description            |
| ------ | ------------------------------------- | ---------------------- |
| GET    | /api/executions                       | List executions        |
| GET    | /api/executions/{id}                  | Get execution          |
| GET    | /api/executions/workflow/{workflowId} | Executions by workflow |

---

## Example Workflow Creation

Request:

```http
POST /api/workflows
```

```json
{
  "name": "Employee Onboarding",
  "description": "Enterprise onboarding workflow"
}
```

The Workflow Service stores the workflow and publishes a lifecycle event to Kafka.

---

## Engineering Highlights

### Event-Driven Architecture

* Kafka-based asynchronous communication
* Loose coupling between services
* Independent scalability

### Production Readiness

* Externalized configuration
* Environment-variable based deployment
* Dockerized services
* Cloud database
* Public deployment
* Health endpoints

### Database Management

* Flyway versioned migrations
* Automatic schema initialization
* Cloud PostgreSQL compatibility

### Observability

* Spring Boot Actuator
* Health endpoints
* Metrics support
* Structured service boundaries

---

## Project Documentation

Detailed documentation is available under:

```text
docs/
```

Includes:

* Architecture
* Deployment
* Infrastructure
* Docker
* Neon PostgreSQL
* Render deployment
* Environment variables
* Verification procedures

---

## Resume-Ready Project Summary

**Enterprise Workflow Automation Platform**

Designed and developed a production-grade event-driven microservices platform using **Java 21, Spring Boot 3.5, Apache Kafka, PostgreSQL, Docker, GitHub Actions, Neon, and Render**. Implemented asynchronous workflow lifecycle event processing, REST APIs, Flyway database migrations, cloud deployment, containerized services, CI automation, and comprehensive technical documentation following enterprise backend engineering practices.

---

## Interview Talking Points

This project demonstrates:

* Microservices architecture
* Event-driven systems
* Kafka messaging
* Spring Boot production deployment
* PostgreSQL schema migrations
* Docker containerization
* GitHub Actions CI
* Cloud deployment on Render
* Cloud database integration with Neon
* API documentation with Swagger
* Environment-variable based configuration
* Production profile management
* Enterprise repository organization

---

## Future Enhancements

* Authentication and authorization (JWT / OAuth2)
* Workflow execution engine
* Retry and dead-letter queues
* Saga orchestration
* Outbox pattern implementation
* Redis caching
* Prometheus + Grafana monitoring
* Kubernetes deployment
* Helm charts
* AWS ECS / EKS deployment
* Terraform infrastructure provisioning
* Distributed tracing with OpenTelemetry

---

## Author

**Sahil Biswaprakash Das**

Backend Developer | Java | Spring Boot | Node.js | Microservices | Generative AI | Agentic AI

GitHub:

```text
https://github.com/SAHILDAS
```

Portfolio:

```text
https://portfolio-sahil-blush.vercel.app
```

---

## License

This project is licensed under the MIT License.
