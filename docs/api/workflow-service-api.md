# Workflow Service API

The Workflow Service exposes REST APIs for managing workflow definitions.

## Base URL

```
http://localhost:8081
```

## Swagger UI

```
http://localhost:8081/swagger-ui/index.html
```

## OpenAPI Specification

```
http://localhost:8081/api-docs
```

## Endpoints

| Method | Endpoint                                    | Description         |
| ------ | ------------------------------------------- | ------------------- |
| POST   | `/api/v1/workflows`                         | Create workflow     |
| GET    | `/api/v1/workflows`                         | List workflows      |
| GET    | `/api/v1/workflows/{workflowId}`            | Get workflow by ID  |
| PUT    | `/api/v1/workflows/{workflowId}`            | Update workflow     |
| POST   | `/api/v1/workflows/{workflowId}/activate`   | Activate workflow   |
| POST   | `/api/v1/workflows/{workflowId}/deactivate` | Deactivate workflow |

Interactive documentation is available through Swagger UI.
