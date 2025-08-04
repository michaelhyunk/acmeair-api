package com.acmeair.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        log.debug("Fetching booking with ID {}", id);
        Optional<Booking> result = bookingRepository.findById(id);

        if (result.isEmpty()) {
            log.debug("Booking not found: {}", id);
            log.warn("Booking not found");
            throw new BookingNotFoundException(id);
        }

        return result.get();
    }
    
    // Assumption: One passenger per booking for simplicity.
    public Booking createBooking(BookingRequestDto dto) {
        log.info("Creating new booking");
        log.debug("Attempting to create booking for passenger {} on flight {}",
            dto.getPassengerId(), dto.getFlightId());
        Flight flight = flightRepository.findById(dto.getFlightId())
            .orElseThrow(() -> {
                log.debug("Flight not found: {}", dto.getFlightId());
                log.warn("Flight not found");
                return new FlightNotFoundException(dto.getFlightId());
            });

        if (flight.getBookedSeats() + 1 > flight.getTotalSeats()) {
            log.debug("No seat available on flight {} ({} booked / {} total)",
                flight.getId(), flight.getBookedSeats(), flight.getTotalSeats());
            log.warn("No seat available on flight");
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

        log.info("Booking successfully created");
        log.debug("Booking created: {} (passenger {}, flight {})",
            booking.getId(), dto.getPassengerId(), dto.getFlightId());

        return booking;
    }

    public Booking updateBooking(UUID id, BookingRequestDto dto) {
        log.info("Updating booking");
        log.debug("Updating booking {} with new passenger {} and flight {}",
            id, dto.getPassengerId(), dto.getFlightId());
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> {
                log.debug("Booking to update not found: {}", id);
                log.warn("Booking to update not found");
                return new BookingNotFoundException(id);
            });
        
        booking.setPassengerId(dto.getPassengerId());
        booking.setFlightId(dto.getFlightId());

        Booking updated = bookingRepository.save(booking);
        
        log.info("Booking updated");
        log.debug("Booking {} updated successfully", id);

        return updated;
    }

    public void cancelBooking(UUID id) {
        log.info("Cancelling booking");
        log.debug("Cancelling booking with ID {}", id);
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Booking to cancel not found");
                log.debug("Booking to cancel not found: {}", id);
                return new BookingNotFoundException(id);
            });

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            log.info("Booking already cancelled");
            log.debug("Booking {} is already cancelled — no action taken", id);
            return;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        log.info("Booking cancelled");
        log.debug("Booking {} marked as CANCELLED", id);

        flightRepository.findById(booking.getFlightId()).ifPresent(flight -> {
            flight.setBookedSeats(flight.getBookedSeats() - 1);
            flightRepository.save(flight);
            log.debug("Flight {} seat count adjusted to {}", flight.getId(), flight.getBookedSeats());
        });
    }
}
