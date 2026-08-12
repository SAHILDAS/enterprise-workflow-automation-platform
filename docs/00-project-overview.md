# Project Overview

## Project Name

**Enterprise Workflow Automation Platform**

## Objective

Build a production-quality event-driven workflow orchestration platform capable of defining, versioning, executing, monitoring, and recovering long-running business workflows.

The platform is designed to demonstrate enterprise backend engineering and distributed systems concepts rather than a simple CRUD application.

## Business Problem

Modern enterprises rely on workflows that span multiple systems, asynchronous processes, and human approvals. These workflows require:

* execution state management
* retries
* failure recovery
* event-driven communication
* observability
* scalability
* auditability

This platform provides a generic workflow execution engine that can support scenarios such as employee onboarding, order processing, approval pipelines, and operational automation.

## Demonstration Workflow

The primary demonstration workflow is **Employee Onboarding**.

Example flow:

Employee Created

↓

Create Employee Account

↓

Send Welcome Notification

↓

Create Access Request

↓

Provision Resources

↓

Complete Onboarding

## Architectural Direction

The project uses **two real Spring Boot microservices**:

* **Workflow Service** — owns workflow definitions
* **Execution Service** — owns runtime workflow execution

Communication between services occurs asynchronously through **Apache Kafka**.

Persistent state is stored in **PostgreSQL**, while **Redis** is used for caching and idempotency support.

## Goals

* Demonstrate event-driven architecture
* Implement workflow versioning
* Implement retry and failure recovery
* Use Kafka meaningfully
* Use Redis meaningfully
* Containerize with Docker
* Provide Kubernetes deployment manifests
* Deploy publicly using a free hosting platform
* Produce interview-ready documentation

## Current Phase

Repository Foundation & Architecture
