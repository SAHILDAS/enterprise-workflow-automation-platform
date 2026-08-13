# Local Kafka Setup

This project uses **Apache Kafka 3.8 (KRaft mode)** for asynchronous communication between the Workflow Service and Execution Service. Kafka is started using Docker Compose together with PostgreSQL, Redis, and Kafka UI.

## Services

| Service                       | Port | Purpose                                       |
| ----------------------------- | ---- | --------------------------------------------- |
| Kafka Broker                  | 9092 | Host access for Spring Boot services          |
| Kafka Broker (Docker network) | 9093 | Internal Docker access for Kafka UI           |
| Kafka UI                      | 8085 | Topic, consumer group, and message inspection |

Kafka UI is available at:

`http://localhost:8085`

## Why dual listeners are configured

The project runs **Spring Boot applications on the host machine** while **Kafka UI runs inside Docker**.

Kafka therefore exposes two listeners:

* `PLAINTEXT_HOST://localhost:9092` — used by the Workflow Service and Execution Service
* `PLAINTEXT_DOCKER://kafka:9093` — used by Kafka UI and other Docker containers

This avoids the common issue where Kafka UI attempts to connect to `localhost:9092` from inside the container and remains stuck on the Topics page.

## Docker Compose configuration

The Kafka service is configured with dual listeners:

```yaml
KAFKA_LISTENERS: PLAINTEXT_HOST://:9092,PLAINTEXT_DOCKER://:9093,CONTROLLER://:9094
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT_HOST://localhost:9092,PLAINTEXT_DOCKER://kafka:9093
KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT_DOCKER
```

Kafka UI connects using:

```yaml
KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9093
```

## Starting the infrastructure

From the repository root:

```bash
cd infrastructure/docker
docker compose up -d
```

Verify the containers:

```bash
docker ps
```

Expected containers:

* workflow-postgres
* workflow-redis
* workflow-kafka
* workflow-kafka-ui

## Verifying Kafka

List topics:

```bash
docker exec -it workflow-kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

Expected output:

```text
__consumer_offsets
workflow.lifecycle.events
```

Consume messages:

```bash
docker exec -it workflow-kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic workflow.lifecycle.events \
  --from-beginning
```

## Event flow

Workflow activation publishes a Kafka event:

```text
Client
   |
POST /api/v1/workflows/{id}/activate
   |
Workflow Service
   |
Publish WorkflowActivatedEvent
   |
Kafka Topic: workflow.lifecycle.events
   |
Execution Service
   |
Consume and process event
```

## Kafka UI

Open:

`http://localhost:8085`

Navigate to:

* **Clusters → local**
* **Topics → workflow.lifecycle.events**
* **Consumer Groups → execution-service**

The `execution-service` consumer group confirms that the Execution Service is successfully consuming workflow lifecycle events.

## Troubleshooting

### Kafka UI stuck on “Loading…”

This usually indicates an advertised-listener mismatch.

Verify:

```bash
docker logs workflow-kafka-ui --tail 50
```

The broker should advertise:

* `localhost:9092` for host applications
* `kafka:9093` for Docker containers

### Topic not found

Create the topic:

```bash
docker exec -it workflow-kafka /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --create \
  --topic workflow.lifecycle.events \
  --partitions 3 \
  --replication-factor 1
```

### Reset local Kafka

```bash
docker compose down
docker volume rm docker_kafka_data
docker compose up -d
```

This removes all local Kafka topics and offsets and recreates the broker from scratch.
