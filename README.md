# Client Management API

REST API developed with Java 21 and Spring Boot for managing clients and related resources.

## Technologies

- Java 21
- Spring Boot 3.4.2
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- MySQL / H2
- SpringDoc OpenAPI / Swagger
- Lombok
- AOP

## Features

- CRUD operations for clients and related resources
- DTOs and assemblers
- JPA/Hibernate persistence
- Validation and exception handling
- JWT-based security
- Audit logging with AOP
- Optimistic locking
- Swagger/OpenAPI documentation

## Run locally

The project uses H2 by default, so no external database is required.

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Swagger UI:

`http://localhost:8081/swagger-ui/index.html`

## Database

For local development the API uses an H2 file database under `./data`.

A MySQL configuration can be added later through a separate Spring profile.
