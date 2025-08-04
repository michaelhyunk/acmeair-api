package com.acmeair.api.exception;

public class InvalidBookingStateException extends RuntimeException {
    public InvalidBookingStateException(String message) {
        super(message); //TODO: define what 'invalid' booking is
    }
}
