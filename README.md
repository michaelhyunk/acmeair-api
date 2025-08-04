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
http://localhost:8080/swagger-ui.html
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
| DELETE | `/bookings/{id}`    | Cancel a booking      |

All endpoints return appropriate HTTP status codes and error messages.

---

## Running Tests

```bash
./gradlew test
```

Includes:
- Unit tests for service logic
- Component tests for controller-service integration (mocked repos)

---

## Assumptions Made

- Each booking is for one passenger and one flight only. Did not consider single booking with multiple passengers.
- Because filters with other UUIDs like passengerId, or flightId was not specified, did not fully consider DB relationship. Instead focused heavily on Java and it's difference with .NET
- Booking status is `CONFIRMED` by default, and is `CANCELLED` when cancelled
- Data is stored entirely in memory as per requirement
- No authentication or authorization included
- Passenger IDs are UUIDs with no additional validation
- Branching strategy was not followed on purpose to remove any overheads for branch management. Would've followed if it was specifically mentioned. 

---

## Known Limitations / TODOs

- Global exception formatting (`ErrorResponse`). There's some level of exception formatting, but not enough
- Field-level validation messages returned in structured format
- Pagination for `GET /flights` and `GET /bookings`
- Any files required for deployment automation, like Dockerfile or YML file for Azure App Service
- Integration tests with real database
- API versioning, sorting, filtering
- Any retries with delay + jitter where appropriate

---

## Notes

This project is designed to showcase clean architecture, testable components, and production-readiness patterns.  
Swagger UI is provided for API testing. You’re welcome to extend it further with authentication, external persistence, or UI integrations.
Took extra steps with the requirement purely for my self learning of Java and it's ecosystem, like unit testing controllers and creating endpoints that was not in the requirement, like `getAllBookings` and `getBookingById` in `BookingController.java`.
