# MLOps Workspace API

University coursework REST API built with **JAX-RS (Jersey)** + embedded **Grizzly** HTTP server. No Spring, no database — all data lives in memory in `java.util` collections (`HashMap`/`ArrayList`, via `ConcurrentHashMap`).

## Tech stack
- Java 17
- Maven
- Jersey 3.1.x (Jakarta RESTful Web Services)
- Embedded Grizzly HTTP server
- Jackson (JSON)

## Project layout

```
src/main/java/
├── Main.java                  # starts the embedded Grizzly server
├── config/ApiApplication.java # @ApplicationPath("/api/v1")
├── model/                     # MLWorkspace, MachineLearningModel, EvaluationMetric
├── storage/DataStore.java     # in-memory singleton store
├── resource/                  # JAX-RS resources
├── exception/                 # custom RuntimeExceptions
├── mapper/                    # ExceptionMappers (incl. global 500 handler)
└── filter/LoggingFilter.java  # logs method/URI/status for every request
```

## Build & run

```bash
# 1. Build
mvn clean package

# 2a. Run directly (recommended for development)
mvn exec:java

# 2b. Or run the packaged executable fat jar
java -jar target/mlops-api.jar
```

The server starts at: `http://localhost:8080/`
API base path: `http://localhost:8080/api/v1`

Stop the server with `CTRL+C`.

## Testing with Postman (or curl)

### 1. Discovery
```
GET http://localhost:8080/api/v1
```
Returns the API map (version, admin contact, resource links).

### 2. Create a workspace
```
POST http://localhost:8080/api/v1/workspaces
Content-Type: application/json

{
  "teamName": "Vision Team",
  "storageQuotaGb": 50
}
```
Response (201): the workspace, with a server-generated `id` and an empty `modelIds` list. **Copy the returned `id`** — you'll need it as `workspaceId` for the next step.

### 3. List / fetch workspaces
```
GET http://localhost:8080/api/v1/workspaces
GET http://localhost:8080/api/v1/workspaces/{workspaceId}
```

### 4. Create a model (linked to the workspace)
```
POST http://localhost:8080/api/v1/models
Content-Type: application/json

{
  "framework": "PyTorch",
  "status": "DEPLOYED",
  "workspaceId": "<paste workspaceId here>"
}
```
Response (201): the model with a server-generated UUID `id`. The workspace's `modelIds` is updated automatically.

Try a bogus `workspaceId` → expect **422** with `{"error":"Referenced workspace does not exist"}`.

### 5. List / filter models
```
GET http://localhost:8080/api/v1/models
GET http://localhost:8080/api/v1/models?status=DEPLOYED
```

### 6. Add an evaluation metric (sub-resource)
```
POST http://localhost:8080/api/v1/models/{modelId}/metrics
Content-Type: application/json

{
  "accuracyScore": 0.93
}
```
Response (201): the metric with server-generated `id`/`timestamp`. The parent model's `latestAccuracy` is updated.

Set the model's `status` to `DEPRECATED` and retry → expect **403** with `{"error":"Model is deprecated"}`.

### 7. List metrics for a model
```
GET http://localhost:8080/api/v1/models/{modelId}/metrics
```

### 8. Delete a non-empty workspace
```
DELETE http://localhost:8080/api/v1/workspaces/{workspaceId}
```
If the workspace still has linked models → expect **409** with `{"error":"Workspace contains models"}`. Remove/disassociate the models first (in this simple in-memory model, that means there's currently no DELETE-model endpoint, so a workspace with models will always 409 — this is intentional per the spec to exercise the exception mapper).

## HTTP status codes used
| Scenario | Status |
|---|---|
| Resource created | 201 |
| Successful GET | 200 |
| Successful DELETE | 204 |
| Workspace/model not found | 404 |
| Delete non-empty workspace | 409 |
| Model references missing workspace | 422 |
| Add metric to deprecated model | 403 |
| Unhandled server error | 500 |

## Logging
Every request/response is logged via `java.util.logging.Logger` (method, URI, and response status) by `LoggingFilter`, visible in the console where the server is running.
