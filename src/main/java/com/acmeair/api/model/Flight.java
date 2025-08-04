package com.acmeair.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Flight(
    UUID id,
    String flightNumber,
    int totalSeats,
    int bookedSeats,
    String origin,
    String destination,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime
) {}
