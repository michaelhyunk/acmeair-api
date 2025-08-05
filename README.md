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

Several endpoints were implemented beyond the brief to support realistic future use cases, such as listing all bookings in addition to retrieving a booking by ID

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
| PUT    | `/bookings/{id}`    | Update passenger contact details (email and note only)     |
| PUT    | `/bookings/{id}/cancel`    | Cancel a booking      |

All endpoints return appropriate HTTP status codes and error messages.
Please refer to the Swagger UI for detailed request/response models, status codes, and query parameters:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Note: Input validation is minimal; request DTOs use `@NotNull` but lack field-level constraints like `@Email` or `@Size`. See "Known Limitations" for more details.

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
- Filtering by secondary identifiers such as `passengerId` or `flightId` was not part of the requirements. As a result, no endpoint-level filters or relational constraints were implemented. The focus remained on core domain logic rather than full query capability or relational modeling
- Each booking is created with a status of `CONFIRMED` by default. The status transitions to `CANCELLED` when the booking is cancelled. No other intermediate statuses (e.g., `PENDING`, `EXPIRED`) are currently defined
- Data is stored entirely in memory as per requirement
- No authentication or authorization included
- Passenger IDs are UUIDs with no additional validation
- Trunk-based development was used instead of branching, considering the scope and time constraints

---

## Known Limitations / TODOs

- Global exception is in place `RestExceptionHandler.java`, however response needs to be defined further
- Basic validation is limited to `@NotNull` on DTOs. Field-level validation (e.g., `@Email`, `@Size`) and enum/value constraints are not yet implemented
- Request DTOs do not validate unknown fields; extra properties in incoming JSON are silently ignored by default
- No rate limiting or API throttling in place. No constraints around requests
- API versioning is not implemented
- Pagination is not implemented for listing endpoints 
- While `ConcurrentHashMap` is used for thread-safe access, no locking or concurrency control is applied. So overbooking may occur in high-concurrency situation
- Dockerfile and/or Azure App Service YAML file for deployment is not included. However, the project is CLI testable and CI/CD-ready. Easily integrated into GitHub Actions or Azure Pipelines
- Integration tests with real database
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
- Basic logging is implemented with info/debug splits. However, could be improved by introducing structured logging (e.g., JSON format), correlation IDs, and sensitive data redaction for production environments
- Swagger is integrated for automated API documentation and contract visibility
- Controller advice is set up for basic error handling, with room for extension
- No exposure of internal state on idempotent operations. If booking is already cancelled, the system returns early without calling the database or logging identifiers
- Repositories are interface-based so switching to a real DB is trivial

### Testability
- Services are unit-tested in isolation with mock dependencies
- Controllers are tested via `@WebMvcTest` and mocked services
- Tests are organized by responsibility and cover both positive and negative scenarios
- Unit tests can be executed via CLI and easily integrated into CI/CD pipelines

## Scaling Considerations

While the current implementation satisfies the core API requirements, the following areas would be important for production-scale deployment:

- **Rate Limiting & Throttling**: To prevent abuse and protect backend systems under high load
- **API Versioning**: To support backward compatibility and safe contract evolution
- **Pagination & Filtering**: For endpoints like `/flights` or `/bookings` to handle large datasets
- **Concurrency Control**: Enforce seat availability rules with synchronized updates or optimistic locking
- **Seat Model & Repository**: Introduce a `Seat` model and `SeatRepository` to manage individual seats, support seat selection, enforce allocation, and handle multi-seat bookings. This allows scaling to seat-class pricing, map-based UI selection, and prevents overbooking via per-seat locking
- **Retry & Backoff**: For transient failures when integrated with external systems (e.g., DB, queues)
- **Persistent Storage**: Swap in-memory storage with real database (JPA/PostgreSQL)
- **Security**: Add authentication (OAuth2/JWT), authorization, and input sanitization
