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
import java.lang.reflect.Method;

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
            "AKL",
            "WLG",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(1)
        );

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        BookingRequestDto dto = new BookingRequestDto(UUID.randomUUID());
        dto.setFlightId(flightId);

        Booking booking = service.createBooking(dto);

        assertNotNull(booking.getId());
        assertEquals(flightId, booking.getFlightId());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldFail_whenFlightNotFound() {
        BookingRequestDto dto = new BookingRequestDto(UUID.randomUUID());
        dto.setFlightId(UUID.randomUUID());

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
            2,
            "AKL",
            "WLG",
            LocalDateTime.now().plusDays(2),
            LocalDateTime.now().plusDays(2).plusHours(2)
        );

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        List<Booking> existingBookings = List.of(
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CONFIRMED),
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CONFIRMED)
        );

        when(bookingRepository.findAll()).thenReturn(existingBookings);

        BookingRequestDto dto = new BookingRequestDto(UUID.randomUUID());
        dto.setFlightId(flightId);

        NoSeatAvailableException thrown = assertThrows(
            NoSeatAvailableException.class,
            () -> service.createBooking(dto));
        
        assertTrue(thrown.getMessage().contains(dto.getFlightId().toString()));
    }

    @Test
    void updateBooking_shouldUpdatePassengerAndFlight() {
        UUID bookingId = UUID.randomUUID();
        UUID originalFlightId = UUID.randomUUID();
        UUID updatedFlightId = UUID.randomUUID();
        UUID passengerId = UUID.randomUUID();

        Booking originalBooking = new Booking(
            bookingId,
            originalFlightId,
            passengerId,
            BookingStatus.CONFIRMED
        );

        BookingRequestDto updateDto = new BookingRequestDto(passengerId);
        updateDto.setFlightId(updatedFlightId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(originalBooking));
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = service.updateBooking(bookingId, updateDto);

        assertEquals(updatedFlightId, result.getFlightId());
        verify(bookingRepository).save(result);
    }

    @Test
    void updateBooking_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        BookingRequestDto dto = new BookingRequestDto(UUID.randomUUID());
        dto.setFlightId(UUID.randomUUID());

        assertThrows(BookingNotFoundException.class, () -> service.updateBooking(id, dto));
    }

    @Test
    void cancelBooking_shouldSetStatusCancelledAndDecrementSeats() {
        UUID bookingId = UUID.randomUUID();
        UUID flightId = UUID.randomUUID();

        Booking booking = new Booking(
            bookingId,
            flightId,
            UUID.randomUUID(),
            BookingStatus.CONFIRMED
        );

        Flight flight = new Flight(
                flightId,
                "NZ123",
                100,
                "AKL",
                "WLG",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        service.cancelBooking(bookingId);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void cancelBooking_shouldDoNothingIfAlreadyCancelled() {
        UUID bookingId = UUID.randomUUID();

        Booking booking = new Booking(
            bookingId,
            UUID.randomUUID(),
            UUID.randomUUID(),
            BookingStatus.CANCELLED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        service.cancelBooking(bookingId);

        // No change, but still verify save is NOT called
        verify(bookingRepository, never()).save(booking);
        verify(flightRepository, never()).save(any());
    }

    @Test
    void cancelBooking_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> service.cancelBooking(id));
    }

    @Test
    void isFlightFull_shouldReturnFalse_whenSeatsAvailable() throws Exception {
        UUID flightId = UUID.randomUUID();

        List<Booking> bookings = List.of(
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CONFIRMED),
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CANCELLED)
        );

        when(bookingRepository.findAll()).thenReturn(bookings);

        Method method = BookingService.class.getDeclaredMethod("isFlightFull", UUID.class, int.class);
        method.setAccessible(true);

        
        boolean result = (boolean) method.invoke(service, flightId, 2);

        assertFalse(result);
    }

    @Test
    void isFlightFull_shouldReturnTrue_whenSeatsFull() throws Exception {
        UUID flightId = UUID.randomUUID();

        List<Booking> bookings = List.of(
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CONFIRMED),
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CONFIRMED)
        );

        when(bookingRepository.findAll()).thenReturn(bookings);

        Method method = BookingService.class.getDeclaredMethod("isFlightFull", UUID.class, int.class);
        method.setAccessible(true);

        
        boolean result = (boolean) method.invoke(service, flightId, 2);

        assertTrue(result);
    }

    @Test
    void getConfirmedBookingCount_shouldIgnoreCancelledBookings() throws Exception {
        UUID flightId = UUID.randomUUID();

        List<Booking> bookings = List.of(
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CONFIRMED),
            new Booking(UUID.randomUUID(), flightId, UUID.randomUUID(), BookingStatus.CANCELLED)
        );

        when(bookingRepository.findAll()).thenReturn(bookings);

        Method method = BookingService.class.getDeclaredMethod("getConfirmedBookingCount",  UUID.class);
        method.setAccessible(true);

        // Use reflection or helper method to call getConfirmedBookingCount
        long count = (long) method.invoke(service, flightId);
        assertEquals(1, count);
    }
}
