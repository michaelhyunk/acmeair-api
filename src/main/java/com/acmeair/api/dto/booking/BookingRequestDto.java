package com.acmeair.api.dto.booking;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;

public class BookingRequestDto {
    @NotNull
    private UUID flightId;

    @NotNull
    private final UUID passengerId;

    @JsonCreator
    public BookingRequestDto(
        @JsonProperty("flightId") UUID flightId,
        @JsonProperty("passengerId") UUID passengerId
    ) {
        this.flightId = flightId;
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
