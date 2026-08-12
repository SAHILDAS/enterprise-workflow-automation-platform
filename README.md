# Enterprise Workflow Automation Platform

Production-grade **event-driven workflow orchestration platform** built with **Java 21, Spring Boot, Apache Kafka, Redis, PostgreSQL, Docker, Kubernetes, and AWS architecture**.

This repository is being developed incrementally as a **portfolio-quality distributed systems project** intended for backend and platform engineering interviews at companies such as **Microsoft, Amazon, Oracle, IBM, Deloitte, Accenture, and product-based technology companies**.

## Project Status

**Current Phase:** Repository Foundation & Architecture

The project is being built from scratch with documented engineering decisions, infrastructure setup, automated testing, containerization, and deployment.

## Architecture Overview

```text
                         Client
                           |
                           v
                    API / Gateway
                           |
             +-------------+-------------+
             |                           |
             v                           v
    +-------------------+       +-------------------+
    | Workflow Service  |       | Execution Service |
    |                   |       |                   |
    | Workflow          |       | WorkflowExecution |
    | WorkflowVersion   |       | StepExecution     |
    | WorkflowStep      |       | Retry Engine      |
    +---------+---------+       +---------+---------+
              |                           |
              +------------+--------------+
                           |
                           v
                      Apache Kafka
                           |
                  +--------+--------+
                  |                 |
                  v                 v
              PostgreSQL          Redis
```

## Core Features

* Workflow definition management
* Workflow versioning
* Asynchronous workflow execution
* Step orchestration
* Retry policies with exponential backoff
* Kafka-based event-driven communication
* Dead-letter handling
* Idempotent event processing
* Redis caching and execution state acceleration
* PostgreSQL persistence
* OpenAPI / Swagger documentation
* Docker Compose local environment
* Kubernetes deployment manifests
* GitHub Actions CI/CD
* Production-style documentation

## Technology Stack

| Layer              | Technology                         |
| ------------------ | ---------------------------------- |
| Language           | Java 21                            |
| Framework          | Spring Boot 3.x                    |
| Messaging          | Apache Kafka                       |
| Database           | PostgreSQL                         |
| Cache              | Redis                              |
| ORM                | Spring Data JPA / Hibernate        |
| Build              | Maven                              |
| Documentation      | OpenAPI / Swagger                  |
| Testing            | JUnit 5, Mockito, Spring Boot Test |
| Containers         | Docker, Docker Compose             |
| Orchestration      | Kubernetes                         |
| Cloud Architecture | AWS (EKS, RDS, ElastiCache, MSK)   |

## Repository Structure

```text
enterprise-workflow-automation-platform/
├── docs/
├── infrastructure/
├── services/
│   ├── workflow-service/
│   └── execution-service/
├── screenshots/
└── .github/
```

## Documentation

Project documentation is maintained in the `docs/` directory.

Key documents:

* Project Overview
* Requirements
* Architecture
* Database Design
* Kafka Event Design
* Redis Strategy
* Reliability & Failure Handling
* Docker & Kubernetes Deployment
* AWS Reference Architecture
* Engineering Decisions
* Interview Preparation Guide

## Development Roadmap

* Repository Foundation
* Maven Multi-Service Setup
* PostgreSQL & Flyway
* Workflow Service
* Execution Service
* Kafka Integration
* Redis Integration
* Testing
* Docker Compose
* Kubernetes
* Free Cloud Deployment
* Documentation & Portfolio Polish

## License

MIT License
