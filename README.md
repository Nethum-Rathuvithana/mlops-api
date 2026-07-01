  MLOps Pipeline Management API (JAX-RS)
  
 Project Overview

This project is a RESTful API built using Jakarta RESTful Web Services (JAX-RS) with an embedded Grizzly HTTP server. It simulates an MLOps (Machine Learning Operations) platform where users can manage ML Workspaces, Machine Learning Models, and Evaluation Metrics.

The system follows REST architectural principles, focusing on resource-based design, stateless communication, and proper HTTP semantics. All data is stored in-memory using Java collections (HashMap, ArrayList) as required by the coursework constraints (no database usage).

Key features include:

Workspace lifecycle management (create, retrieve, delete)
Model registration with workspace validation
Nested resource handling for evaluation metrics
Exception-driven error handling using JAX-RS ExceptionMappers
Request/response logging using filters
JSON serialization using Jackson provider

  How to Run the Project
  
Prerequisites
Java 17+
Maven installed
Internet connection (for dependencies download)
Steps

# 1. Navigate to project folder
cd mlops-api

# 2. Build project
mvn clean install

# 3. Run server
mvn exec:java

Server Base URL

http://localhost:8080/api/v1

  Sample API Testing (curl examples)
  
1. Discovery Endpoint
curl http://localhost:8080/api/v1/

3. Create Workspace
curl -X POST http://localhost:8080/api/v1/workspaces \
-H "Content-Type: application/json" \
-d '{"teamName":"AI Lab","storageQuotaGb":50}'

5. Get All Workspaces
curl http://localhost:8080/api/v1/workspaces

7. Create Model
curl -X POST http://localhost:8080/api/v1/models \
-H "Content-Type: application/json" \
-d '{"framework":"TensorFlow","status":"TRAINING","workspaceId":"<workspace-id>"}'

9. Add Evaluation Metric
curl -X POST http://localhost:8080/api/v1/models/<model-id>/metrics \
-H "Content-Type: application/json" \
-d '{"accuracyScore":0.92}'

  PART 1: Setup & Discovery
  
Q1: Role of MessageBodyWriter / JSON Provider

In JAX-RS, when a Java object is returned from a REST endpoint, it must be converted into a JSON representation before being sent over HTTP. This conversion is handled by a MessageBodyWriter, which is part of the JAX-RS runtime.

In this project, Jackson (jersey-media-json-jackson) acts as the JSON provider. It automatically serializes Java POJOs into JSON and deserializes JSON into Java objects.

This abstraction allows developers to focus on business logic while the framework handles object mapping, ensuring consistency and reducing boilerplate code.

Q2: Statelessness in REST

REST APIs are stateless, meaning that each HTTP request is independent and must contain all necessary information for processing. The server does not store client session data between requests.

This improves scalability because any server instance can handle any request without relying on previous interactions. In cloud environments, this enables horizontal scaling behind load balancers, improving reliability and performance.

Statelessness also simplifies recovery, monitoring, and system design since no server-side session synchronization is required.

  PART 2: Workspace Management
  
Q1: Cache-Control Benefits

HTTP Cache-Control headers allow responses to be stored temporarily by clients or intermediate proxies. For the GET /workspaces endpoint, caching reduces redundant server processing and database (or in-memory store) reads.

This improves:

Response time for clients
Server performance under high load
Network efficiency by reducing repeated data transfer

In large-scale systems, caching significantly improves scalability and reduces operational cost.

Q2: HEAD vs GET Method

The HEAD method should be used when a client wants to verify resource existence without downloading the response body.

Unlike GET, HEAD returns only headers, making it lightweight and bandwidth-efficient. This is useful for checking whether a workspace exists without retrieving full JSON data.

  PART 3: Model Operations
  
Q1: Why Server Generates UUID

Allowing the server to generate unique identifiers ensures:

Data integrity (no duplicate or conflicting IDs)
Security (prevents client-side ID manipulation)
Centralized control of resource identity

Using UUID.randomUUID() guarantees uniqueness across distributed systems and avoids collisions that may occur if clients generate their own IDs.

Q2: URL Encoding Requirement

When query parameters contain spaces or special characters, they must be URL encoded to ensure safe transmission over HTTP.

For example:

Scikit Learn & Tools → Scikit%20Learn%20%26%20Tools

This is necessary because URLs follow a strict format defined by RFC standards, and unencoded characters may break parsing or lead to incorrect request interpretation.

  PART 4: Sub-Resources
  
Q1: Class-level vs Method-level @Produces

Class-level @Produces applies a default response format to all methods in a resource class. Method-level @Produces overrides this default for specific endpoints.

This improves maintainability by reducing repetition while still allowing fine-grained control over individual responses.

Q2: Side-effect update in Metrics

When a new evaluation metric is added, the system updates the parent model’s latestAccuracy. This ensures consistency between historical metrics and the current model state.

This design reflects real-world MLOps systems where model performance tracking must remain synchronized with evaluation history.

  PART 5: Error Handling & Logging
  
Q1: 4xx vs 5xx Status Codes

4xx status codes indicate client-side errors, where the request is invalid (e.g., missing workspace or invalid input). 5xx errors represent server-side failures.

A missing workspace ID is a client validation issue because the server is functioning correctly but cannot process invalid input. Therefore, it correctly returns a 4xx status (422/400).

Q2: Exception Mapper Priority

JAX-RS selects the most specific ExceptionMapper available. If a custom mapper exists for a specific exception type, it takes priority over generic mappers like Throwable.

This ensures precise error handling while still allowing a global fallback for unexpected exceptions.

Q3: Logging Filter Metadata

From ContainerRequestContext and ContainerResponseContext, we can extract:

HTTP method (GET, POST, DELETE)
Request URI
Headers (e.g., Authorization, Content-Type)
Response status code

This information is essential for debugging, monitoring, and tracing API behavior in production systems.

  Summary

This API demonstrates:

RESTful architecture principles
Proper resource modelling
Nested sub-resource design
Exception-driven error handling
Stateless scalable design
Real-world MLOps simulation

  Final Note

This project strictly follows coursework constraints:

 JAX-RS only (no Spring Boot)
 No database (in-memory collections only)
 Proper exception mapping
 Embedded Grizzly server
 JSON via Jackson provider
