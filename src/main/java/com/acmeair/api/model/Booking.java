package com.acmeair.api.model;

import java.util.UUID;

// Assumption here is that passengerId is immutable and should use builder pattern in unit tests instead.

public class Booking {
    private final UUID id;
    private final UUID flightId;
    private final Passenger passenger;
    private BookingStatus status;

    public Booking(UUID id, UUID flightId, Passenger passenger, BookingStatus status) {
        this.id = id;
        this.flightId = flightId;
        this.passenger = passenger;
        this.status = status;
    }

    public UUID getId() { return id; }

    public UUID getFlightId() { return flightId; }

    public Passenger getPassenger() { return passenger; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}