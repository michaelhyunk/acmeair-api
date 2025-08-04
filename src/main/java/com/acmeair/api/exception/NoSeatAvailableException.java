package com.acmeair.api.exception;

import java.util.UUID;

public class NoSeatAvailableException extends RuntimeException {
    public NoSeatAvailableException(UUID flightId) {
        super("No seats available for flight ID: " + flightId);
    }
}
