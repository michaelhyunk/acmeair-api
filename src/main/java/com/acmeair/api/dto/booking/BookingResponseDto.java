package com.acmeair.api.dto.booking;

import com.acmeair.api.model.BookingStatus;
import com.acmeair.api.model.Passenger;
import java.util.UUID;

public class BookingResponseDto {
    private UUID bookingId;
    private Passenger passenger;
    private UUID flightId;
    private BookingStatus status;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
