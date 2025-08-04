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

    // Assumption here is that each booking only has 1 passenger
    // @Min(1)
    // private int numberOfSeats;

    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public UUID getPassengerId() {
        return passengerId;
    }

    // public int getNumberOfSeats() {
    //     return numberOfSeats;
    // }

    // public void setNumberOfSeats(int numberOfSeats) {
    //     this.numberOfSeats = numberOfSeats;
    // }
}
