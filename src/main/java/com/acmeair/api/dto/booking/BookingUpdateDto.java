package com.acmeair.api.dto.booking;

import com.acmeair.api.model.Passenger;
import java.util.UUID;
import jakarta.validation.constraints.*;

public class BookingUpdateDto {
    @NotNull
    private UUID flightId;

    @NotNull
    private Passenger passenger;

    public BookingUpdateDto() {}

    public BookingUpdateDto(UUID flightId, Passenger passenger) {
        this.flightId = flightId;
        this.passenger = passenger;
    }

    public UUID getFlightId() {
        return flightId;
    }

    public void setFLightId(UUID flightId) {
        this.flightId = flightId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
