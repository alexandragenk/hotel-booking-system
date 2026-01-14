# Hotel Booking System

Educational project: a microservices-based hotel booking system using **Spring Boot**.

The project consists of four Spring Boot applications:

- **eureka-server** – service registry (Service Discovery)
- **api-gateway** – gateway (Spring Cloud Gateway)
- **booking-service** – users, authentication, bookings
- **hotel-service** – hotels, rooms, occupancy statistics, availability confirmation

Each service uses an in-memory **H2** database.

---

## How to run the system

### Build

```bash
./gradlew clean build
```

### Start services
```bash
./gradlew clean runAll
```

### Run tests
```bash
./gradlew test
```

## API

### [Booking Service](booking-service/openapi.yaml)

### [Hotel Service](hotel-service/openapi.yaml)