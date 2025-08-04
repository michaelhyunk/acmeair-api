package com.acmeair.api.service;

import com.acmeair.api.model.Booking;
import com.acmeair.api.model.BookingStatus;
import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.BookingRepository;
import com.acmeair.api.repository.FlightRepository;
import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.exception.BookingNotFoundException;
import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.exception.NoSeatAvailableException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    private BookingRepository bookingRepository;
    private FlightRepository flightRepository;
    private BookingService service;

    @BeforeEach
    void setup() {
        bookingRepository = mock(BookingRepository.class);
        flightRepository = mock(FlightRepository.class);
        service = new BookingService(bookingRepository, flightRepository);
    }

    @Test
    void getAllBookings_shouldReturnListOfBookings() {
        List<Booking> bookings = List.of(
            new Booking(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BookingStatus.CONFIRMED
            ),
            new Booking(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BookingStatus.CANCELLED
            )
        );
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = service.getAllBookings();

        assertEquals(2, result.size());
    }

    @Test
    void getBookingById_shouldReturnBooking() {
        UUID id = UUID.randomUUID();
        Booking booking = new Booking(
                id,
                UUID.randomUUID(),
                UUID.randomUUID(),
                BookingStatus.CONFIRMED
            );
        
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        Booking result = service.getBookingById(id);

        assertEquals(booking, result);
    }

    @Test
    void getBookingById_shouldThrowBookingNotFound() {
        UUID id = UUID.randomUUID();
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        BookingNotFoundException thrown = assertThrows(
            BookingNotFoundException.class,
            () -> service.getBookingById(id)
        );

        assertTrue(thrown.getMessage().contains(id.toString()));
    }

    @Test
    void createBooking_shouldSucceed_whenSeatsAvailable() {
        UUID flightId = UUID.randomUUID();
        Flight flight = new Flight(
            flightId,
            "NZ123",
            100,
            10,
            "AKL",
            "WLG",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(1)
        );

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        BookingRequestDto dto = new BookingRequestDto();
        dto.setFlightId(flightId);
        dto.setPassengerId(UUID.randomUUID());

        Booking booking = service.createBooking(dto);

        assertNotNull(booking.getId());
        assertEquals(flightId, booking.getFlightId());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        verify(bookingRepository).save(any(Booking.class));
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void createBooking_shouldFail_whenFlightNotFound() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setFlightId(UUID.randomUUID());
        dto.setPassengerId(UUID.randomUUID());

        FlightNotFoundException thrown = assertThrows(
            FlightNotFoundException.class,
            () -> service.createBooking(dto));

        assertTrue(thrown.getMessage().contains(dto.getFlightId().toString()));
    }

    @Test
    void createBooking_shouldFail_whenSeatsAreNotAvailable() {
        UUID flightId = UUID.randomUUID();
        Flight flight = new Flight(
            flightId,
            "NZ123",
            50,
            50,
            "AKL",
            "WLG",
            LocalDateTime.now().plusDays(2),
            LocalDateTime.now().plusDays(2).plusHours(2)
        );

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        BookingRequestDto dto = new BookingRequestDto();
        dto.setFlightId(flightId);
        dto.setPassengerId(UUID.randomUUID());

        NoSeatAvailableException thrown = assertThrows(
            NoSeatAvailableException.class,
            () -> service.createBooking(dto));
        
        assertTrue(thrown.getMessage().contains(dto.getFlightId().toString()));
    }
}
