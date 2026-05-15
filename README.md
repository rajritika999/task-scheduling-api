mvn# Task Scheduling API

A simple Spring Boot REST API for managing scheduled tasks using an in-memory repository.

## Features

- Create, read, update, and delete tasks
- Validate create/update request payloads
- Task status tracking with `PENDING`, `IN_PROGRESS`, and `DONE`
- Task list sorted by due date
- Pagination and status filtering for task lists
- Lightweight Spring Boot application using Maven

## Technology stack

- Java 17
- Spring Boot 3.5.14
- Spring Web
- Spring Validation
- Lombok
- Maven (wrapper included, no installation required)

## Project structure

- `src/main/java/com/taskscheduling/api/controller` - REST controller
- `src/main/java/com/taskscheduling/api/service` - service layer and business logic
- `src/main/java/com/taskscheduling/api/repository` - in-memory task repository
- `src/main/java/com/taskscheduling/api/model` - task model and status enum
- `src/main/java/com/taskscheduling/api/dto` - request and response DTOs
- `src/test/java` - unit tests

## Endpoints

Base URL: `http://localhost:8080/tasks`

- `POST /tasks`
  - Create a new task
  - Required fields: `title`, `dueDate`
  - Optional fields: `description`, `status`
  - `dueDate` must be a future date

- `GET /tasks/{id}`
  - Get a task by ID

- `GET /tasks`
  - Get all tasks sorted by due date
  - Supports optional filtering by `status`
  - Supports pagination with `page` and `size`

- `PUT /tasks/{id}`
  - Update an existing task
  - Supports partial updates for `title`, `description`, `status`, and `dueDate`

- `DELETE /tasks/{id}`
  - Delete a task by ID

## Request examples

### Create task

```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Finish report",
    "description": "Complete the quarterly report",
    "dueDate": "2026-05-31",
    "status": "PENDING"
  }'
```

### Update task

```bash
curl -X PUT http://localhost:8080/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS"
  }'
```

### Get all tasks

```bash
curl http://localhost:8080/tasks
```

### Get filtered and paginated tasks

```bash
curl "http://localhost:8080/tasks?status=PENDING&page=0&size=5"
```

## Installation and dependencies

### Required software

- Java 17 JDK

### Optional software

- Maven (not required, as the project includes the Maven wrapper which downloads Maven automatically)

### Install Java 17

For Windows:

```powershell
choco install openjdk17
```

For macOS:

```bash
brew install openjdk@17
```

For Ubuntu / Debian:

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

Verify your Java installation:

```bash
java -version
```

The output should include `17`.

### Install Maven (optional)

This project includes the Maven wrapper, so Maven is not strictly required. The wrapper will download Maven automatically if needed.

If you prefer to install Maven globally:

For Windows:

```powershell
choco install maven
```

For macOS:

```bash
brew install maven
```

For Ubuntu / Debian:

```bash
sudo apt update
sudo apt install maven
```

Verify Maven:

```bash
mvn -version
```

## Build and run

The project includes `mvnw` (for macOS/Linux) and `mvnw.cmd` (for Windows) Maven wrapper scripts, so you can build and run without installing Maven globally. The wrapper will automatically download Maven if it's not already available.

### Build

For Windows:

```powershell
.\mvnw.cmd clean package
```

For macOS/Linux:

```bash
./mvnw clean package
```

### Run

For Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

For macOS/Linux:

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

### Alternative: Using installed Maven

If you have Maven installed globally, you can use `mvn` instead of `mvnw` or `mvnw.cmd`:

```bash
mvn clean package
mvn spring-boot:run
mvn test
```

## Tests

For Windows:

```powershell
.\mvnw.cmd test
```

For macOS/Linux:

```bash
./mvnw test
```

### Alternative: Using installed Maven

If you have Maven installed globally:

```bash
mvn test
```

## Notes

- Data is stored in memory and will not persist after the application stops.
- Validation is enabled for create and update requests using Jakarta Validation annotations.
- `dueDate` values must be in the future.
- The application currently does not include a database or authentication.
