package com.acmeair.api.service;

import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.exception.BookingNotFoundException;
import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.exception.NoSeatAvailableException;
import com.acmeair.api.model.Booking;
import com.acmeair.api.model.BookingStatus;
import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.FlightRepository;
import com.acmeair.api.repository.BookingRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(UUID id) {
        Optional<Booking> result = bookingRepository.findById(id);

        if (result.isEmpty()) {
            throw new BookingNotFoundException(id);
        }

        return result.get();
    }
    
    // Assumption: One passenger per booking for simplicity.
    public Booking createBooking(BookingRequestDto dto) {
        Flight flight = flightRepository.findById(dto.getFlightId())
            .orElseThrow(() -> new FlightNotFoundException(dto.getFlightId()));

        if (flight.getBookedSeats() + 1 > flight.getTotalSeats()) {
            throw new NoSeatAvailableException(flight.getId());
        }

        Booking booking = new Booking(
            UUID.randomUUID(),
            dto.getFlightId(),
            dto.getPassengerId(),
            BookingStatus.CONFIRMED
        );

        bookingRepository.save(booking);

        flight.setBookedSeats(flight.getBookedSeats() + 1);
        flightRepository.save(flight);

        return booking;
    }
}
