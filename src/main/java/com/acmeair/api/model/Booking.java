package com.acmeair.api.model;

import java.util.UUID;

// Assumption here is that passengerId is immutable and should use builder pattern in unit tests instead.

public class Booking {
    private UUID id;
    private UUID flightId;
    private UUID passengerId;
    private BookingStatus status;

    public Booking() {}

    public Booking(UUID id, UUID flightId, UUID passengerId, BookingStatus status) {
        this.id = id;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getFlightId() { return flightId; }
    public void setFlightId(UUID flightId) { this.flightId = flightId; }

    public UUID getPassengerId() { return passengerId; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}