package com.acmeair.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.dto.booking.BookingUpdateDto;
import com.acmeair.api.exception.BookingNotFoundException;
import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.exception.NoSeatAvailableException;
import com.acmeair.api.model.Booking;
import com.acmeair.api.model.BookingStatus;
import com.acmeair.api.model.Flight;
import com.acmeair.api.model.Passenger;
import com.acmeair.api.repository.FlightRepository;
import com.acmeair.api.repository.BookingRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    public Booking getBookingById(UUID id) {
        Optional<Booking> result = bookingRepository.findById(id);

        if (result.isEmpty()) {
            log.warn("Booking not found");
            throw new BookingNotFoundException(id);
        }

        return result.get();
    }
    
    // Assumption: One passenger per booking for simplicity.
    public Booking createBooking(BookingRequestDto dto) {
        log.info("Creating new booking");
        Flight flight = flightRepository.findById(dto.getFlightId())
            .orElseThrow(() -> {
                log.warn("Flight not found");
                return new FlightNotFoundException(dto.getFlightId());
            });

        if (isFlightFull(dto.getFlightId(), flight.getTotalSeats())) {
            log.warn("No seat available on flight");
            throw new NoSeatAvailableException(flight.getId());
        }

        Passenger passenger = new Passenger(
            UUID.randomUUID(),
            "Joe",
            "Smith",
            "Joe.Smith@acmeair.co.nz",
            "Prefers window seat"
        );

        Booking booking = new Booking(
            UUID.randomUUID(),
            dto.getFlightId(),
            passenger,
            BookingStatus.CONFIRMED
        );

        bookingRepository.save(booking);

        log.info("Booking successfully created");

        return booking;
    }

    public Booking updateBooking(UUID id, BookingUpdateDto dto) {
        log.info("Updating passenger details for existing booking");
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Booking to update not found");
                return new BookingNotFoundException(id);
            });

        Passenger existing = booking.getPassenger();
        Passenger updated = dto.getPassenger();

        if (!existing.getId().equals(updated.getId())) {
            throw new IllegalArgumentException("Passenger ID cannot be changed");
        }

        if (!existing.getFirstName().equals(updated.getFirstName()) ||
            !existing.getLastName().equals(updated.getLastName())) {
            throw new IllegalArgumentException("Passenger name cannot be changed");
        }

        if (!booking.getFlightId().equals(dto.getFlightId())) {
            throw new IllegalArgumentException("Flight ID cannot be changed");
        }

        existing.setEmail(updated.getEmail());
        existing.setPassengerNote(updated.getPassengerNote());

        log.info("Updated passenger detail for an existing booking");

        Booking updatedBooking = bookingRepository.save(booking);
        return updatedBooking;
    }

    public void cancelBooking(UUID id) {
        log.info("Cancelling booking");
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Booking to cancel not found");
                return new BookingNotFoundException(id);
            });

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            log.info("Booking already cancelled");
            return;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        log.info("Booking cancelled");

        bookingRepository.save(booking);
    }

    private boolean isFlightFull(UUID flightId, int totalSeats) {
        return getConfirmedBookingCount(flightId) >= totalSeats;
    }

    private long getConfirmedBookingCount(UUID flightId) {
        return bookingRepository.findAll().stream()
            .filter(b -> b.getFlightId().equals(flightId))
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
            .count();
    }
}
