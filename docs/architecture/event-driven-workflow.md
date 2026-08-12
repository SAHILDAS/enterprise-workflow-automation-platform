# Event-driven workflow communication

## Topic

`workflow.lifecycle.events`

## Producer

Workflow Service publishes lifecycle events when workflows are activated.

## Consumer

Execution Service consumes lifecycle events asynchronously.

## Benefits

* Loose coupling
* Asynchronous communication
* Independent scaling
* Event replay capability
* Audit-friendly architecture

## Flow

Client → Workflow Service → Kafka → Execution Service
