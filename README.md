# AcmeAir API

AcmeAir is a fictional airline booking REST API built with Spring Boot 3.4.8.  
This service allows users to search for flights, book a flight, view or cancel existing bookings — all using in-memory storage.

---

## Features Implemented

- Search for available flights
- Retrieve a flight by ID
- Create and save a booking
- Retrieve a booking by ID
- Update an existing booking
- Cancel a booking

Several endpoints were implemented beyong the brief to reflect realistic future needs, such as retrieving all bookins or accessing a specific booking by ID.

---

## Tech Stack

| Layer        | Tech                                  |
|--------------|---------------------------------------|
| Language     | Java 21                               |
| Framework    | Spring Boot 3.4.8                     |
| Build Tool   | Gradle (Kotlin DSL)                   |
| Storage      | In-memory (ConcurrentHashMap)         |
| Docs         | Swagger / OpenAPI 3 (`springdoc-openapi`) |
| Mapping      | MapStruct                             |
| Testing      | JUnit 5 + Spring Boot Starter Test    |

---

## Getting Started

### 1. Clone & Build
```bash
git clone <your-repo-url>
cd acmeair-api
./gradlew clean build
```

### 2. Run the Application
```bash
./gradlew bootRun
```

### 3. Open the Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## API Endpoints

| Method | Endpoint            | Description           |
|--------|---------------------|-----------------------|
| GET    | `/flights`          | List all flights      |
| GET    | `/flights/{id}`     | Get flight by ID      |
| GET    | `/bookings`         | List all bookings     |
| GET    | `/bookings/{id}`    | Get booking by ID     |
| POST   | `/bookings`         | Create a booking      |
| PUT    | `/bookings/{id}`    | Update a booking      |
| PUT    | `/bookings/{id}/cancel`    | Cancel a booking      |

All endpoints return appropriate HTTP status codes and error messages.
Please refer to swagger for more information:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Running Tests

```bash
./gradlew test
```

Test coverage was developed using a TDD (Test-Driven Development) approach, ensuring that business logic is verified before implementation. It includes:
- Unit tests for service logic
- Component tests for controller-service integration (mocked repos)
- Failure cases (e.g. not found, no seat available)
- Behavior-driven coverage to reflect requirement scenarios

You should not need to manually verify functionality.

---

## Assumptions Made

- Each booking is for one passenger and one flight only. Did not consider single booking with multiple passengers
- For Booking and BookingRequestDto, passengerId is immutable and only flightId is editable
- Seat-level tracking is not implemented. Each booking assumes 1-to-1 relationship between a passenger and a seat, only total seat availability is checked
- A `Seat` model was intentionally omitted to keep domain lightweight. This can be introduced later if seat selection, multiple seat bookings become relevant
- Because filters with other UUIDs like passengerId, or flightId was not specified, did not fully consider DB relationship. Instead focused on domain logic and structural differences vs .NET
- Booking status is `CONFIRMED` by default, and is `CANCELLED` when cancelled
- Data is stored entirely in memory as per requirement
- No authentication or authorization included
- Passenger IDs are UUIDs with no additional validation
- Trunk-based development was used instead of branching, considering the scope and time constraints

---

## Known Limitations / TODOs

- Global exception formatting (`ErrorResponse`) structure needs improvement
- Validation errors are not returned in a consistent, client-consumable format
- Pagination is not implemented for listing endpoints
- While `ConcurrentHashMap` is used for thread-safe access, no locking or concurrency control is applied. So overbooking may occur in high-concurrency situation.
- Dockerfile and/or Azure App Service YAML file for deployment is not included
- Integration tests with real database
- API versioning, sorting, filtering
- Retry/backoff strategies (e.g. jitter/delay) are not yet implemented
- Introduce `TestData.java` as a centralized test fixture to streamline setup and remove duplicated data creation in unit tests

---

## Notes

### Clean Architecture
- Controllers are kept thin and delegate business logic to services
- DTOs isolate API contracts from internal models
- Repositories are defined via interfaces to support abstraction, flexibility and testability. Also makes it easy to swap out the in-memory repositories with real implementations (e.g., JPA, JDBC, Mongo)
- Mappers translate between layers via MapStruct to reduce boilerplate 

### Production-Readiness
- Logging follows best practices (info/debug split, redacted UUIDs in prod)
- Swagger is integrated for automated API documentation and contract visibility
- Controller advice is set up for basic error handling, with room for extension
- No exposure of internal state on idempotent operations. If booking is already cancelled, the system returns early without calling the database or logging identifiers
- Repositories are interface-based so switching to a real DB is trivial

### Testability
- Services are unit-tested in isolation with mock dependencies
- Controllers are tested via `@WebMvcTest` and mocked services
- Tests are split by responsibility and cover both success and failure paths
- Unit tests can be run via CLI and can easily add a job in CI/CD pipelines on build
