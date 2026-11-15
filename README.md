# Student Management System REST API

A small reference project using Java 21, Spring Boot 3.5, Spring MVC, Spring Data JPA, Hibernate, and MySQL.

## 1. Requirements

- JDK 21
- IntelliJ IDEA (Maven support)
- MySQL 8.x
- Maven 3.6.3+ (or use the Maven wrapper if you add one)

Spring Boot manages compatible Spring, Hibernate, and Spring Data versions. Avoid setting individual versions for those dependencies unless you have a specific reason.

## 2. Create the database

Log in to MySQL as an administrator and execute `database/create-database.sql`.

For a local learning database, the script creates:
- database: `student_db_codex`
- account: `student_app_codex`
- example password: `123456`

Change the example password before using this beyond a local demo. If MySQL is in Docker or on another machine, adjust the database host, user host permissions, and `DB_URL`.

## 3. Open in IntelliJ IDEA

1. Extract this folder.
2. Choose **File → Open**, then select the folder containing `pom.xml`.
3. Set Project SDK to JDK 21.
4. Allow Maven to import dependencies.
5. Ensure `application.yml` credentials match your MySQL setup.
6. Run `StudentManagementApplication`.

The API listens on `http://localhost:8082`.

You can override database settings using environment variables:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## 4. Try the API

Create a student:

```bash
curl -i -X POST http://localhost:8082/api/students \
  -H 'Content-Type: application/json' \
  -d '{
    "studentNumber": "S2026001",
    "name": "Alice Zhang",
    "email": "alice@example.com",
    "age": 20,
    "major": "Computer Science"
  }'
```

List students:

```bash
curl -i 'http://localhost:8082/api/students'
```

Pagination:

```bash
curl -i 'http://localhost:8082/api/students?page=0&size=5'
```

Search by name or email:

```bash
curl -i 'http://localhost:8082/api/students?keyword=alice'
```

Get a student:

```bash
curl -i http://localhost:8082/api/students/1
```

Update a student (PUT replaces the editable fields; include all required fields):

```bash
curl -i -X PUT http://localhost:8082/api/students/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "studentNumber": "S2026001",
    "name": "Alice Zhang",
    "email": "alice@example.com",
    "age": 21,
    "major": "Software Engineering"
  }'
```

Delete a student:

```bash
curl -i -X DELETE http://localhost:8082/api/students/1
```

Expected status codes:
- `201 Created`: created successfully
- `200 OK`: read or update succeeded
- `204 No Content`: deleted successfully
- `400 Bad Request`: invalid JSON or validation error
- `404 Not Found`: student ID does not exist
- `409 Conflict`: duplicate student number/email or database constraint conflict

## 5. Project structure

```text
src/main/java/com/example/studentmanagement/
├── StudentManagementApplication.java
├── common/
│   ├── ApiError.java
│   ├── ConflictException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
└── student/
    ├── Student.java
    ├── StudentController.java
    ├── StudentRepository.java
    ├── StudentRequest.java
    ├── StudentResponse.java
    └── StudentService.java
```

## 6. Request flow

```text
HTTP request / JSON
       ↓
StudentController  — routing, HTTP status, request validation
       ↓
StudentService     — business rules and transaction boundary
       ↓
StudentRepository — Spring Data JPA-generated data access
       ↓
Hibernate / JPA    — object-relational mapping and SQL
       ↓
MySQL              — persistent storage
```

## 7. Important learning notes

- `@RestController` exposes HTTP endpoints and serializes return values as JSON.
- `@Valid` activates Jakarta Bean Validation on request DTOs.
- `StudentRequest` is an input DTO; `StudentResponse` is an output DTO. This avoids binding HTTP requests directly to a persistence entity.
- `@Entity` and `@Table` map `Student` to the `students` table.
- `JpaRepository` supplies common CRUD and pagination operations.
- `@Service` holds business rules; constructor injection supplies the repository.
- `@Transactional` defines a transaction boundary around service operations.
- `@RestControllerAdvice` centralizes API error responses.
- The database unique constraints protect against duplicates even if concurrent requests race.
- `ddl-auto: update` is convenient for learning, but production systems should use schema migrations (for example, Flyway) and normally set Hibernate DDL generation to `validate` or `none`.
- This is a learning demo, not a production-ready deployment: it does not include authentication, authorization, rate limiting, audit logging, or comprehensive automated tests.
