package com.acmeair.api.dto.booking;

import com.acmeair.api.model.BookingStatus;
import java.util.UUID;

public class BookingResponseDto {
    private UUID bookingId;
    private UUID passengerId;
    private UUID flightId;
    private BookingStatus status;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(UUID passengerId) {
        this.passengerId = passengerId;
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
