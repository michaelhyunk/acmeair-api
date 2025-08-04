package com.acmeair.api.exception;

import java.util.UUID;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(UUID flightId) {
        super("Flight not found with ID: " + flightId);
    }
}
