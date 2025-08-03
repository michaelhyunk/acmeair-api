package com.acmeair.api.model;

import java.util.UUID;

public record Booking(
    UUID id,
    UUID flightId,
    UUID passengerId,
    BookingStatus status
) {}