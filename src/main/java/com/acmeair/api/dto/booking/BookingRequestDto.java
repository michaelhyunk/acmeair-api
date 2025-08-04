package com.acmeair.api.dto.booking;

import java.util.UUID;
import jakarta.validation.constraints.*;

public class BookingRequestDto {
    @NotNull
    private UUID flightId;

    @NotNull
    private final UUID passengerId;

    public BookingRequestDto(UUID passengerId) {
        this.passengerId = passengerId;
    }

    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public UUID getPassengerId() {
        return passengerId;
    }
}
