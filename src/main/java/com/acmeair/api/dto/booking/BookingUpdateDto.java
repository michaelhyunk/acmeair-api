package com.acmeair.api.dto.booking;

import jakarta.validation.constraints.NotBlank;

public class BookingUpdateDto {
    @NotBlank
    private String passengerName;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}
