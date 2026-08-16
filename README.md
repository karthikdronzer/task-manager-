# Task Manager REST API (Spring Boot)

A complete, ready-to-run Task Management REST API built with **Spring Boot 3**, **Spring Data JPA**, **Bean Validation**, and an in-memory **H2** database. Layered architecture: Controller → Service → Repository → Entity.

## Run it

```bash
mvn spring-boot:run
```

App starts on **http://localhost:8080**. Seed data loads automatically (4 sample tasks).

H2 console (view the DB in browser): http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:taskdb`
- User: `sa`, Password: (empty)

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/tasks` | List all tasks |
| GET | `/api/tasks/{id}` | Get one task |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |
| GET | `/api/tasks/status/{status}` | Filter by status (PENDING / IN_PROGRESS / COMPLETED) |
| GET | `/api/tasks/search?keyword=x` | Search by title |
| PATCH | `/api/tasks/{id}/complete` | Mark task as completed |

### Create task example

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "priority": 2,
    "dueDate": "2026-08-20"
  }'
```

### Update task example

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "status": "IN_PROGRESS",
    "priority": 1,
    "dueDate": "2026-08-18"
  }'
```

### Mark complete

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/complete
```

## Project structure

```
src/main/java/com/example/taskmanager/
├── TaskManagerApplication.java
├── controller/TaskController.java
├── service/TaskService.java
├── repository/TaskRepository.java
├── entity/Task.java, TaskStatus.java
├── dto/TaskRequest.java, TaskResponse.java
└── exception/GlobalExceptionHandler.java, ResourceNotFoundException.java, ErrorResponse.java
```

Validation errors return HTTP 400 with a field-error map. Missing tasks return HTTP 404 with a message. Requires Java 17+ and Maven.
