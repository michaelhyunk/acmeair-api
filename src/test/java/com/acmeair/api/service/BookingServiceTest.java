package com.acmeair.api.service;

import com.acmeair.api.model.Booking;
import com.acmeair.api.repository.BookingRepository;
import com.acmeair.api.exception.BookingNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    private BookingRepository repository;
    private BookingService service;

    @BeforeEach
    void setup() {
        repository = mock(BookingRepository.class);
        service = new BookingService(repository);
    }

    @Test
    void getAllBookings_shouldReturnListOfBookings() {
        List<Booking> bookings = List.of(new Booking(), new Booking());
        when(repository.findAll()).thenReturn(bookings);

        List<Booking> result = service.getAllBookings();

        assertEquals(2, result.size());
    }

    @Test
    void getBookingById_shouldReturnBooking() {
        UUID id = UUID.randomUUID();
        Booking booking = new Booking();
        when(repository.findById(id)).thenReturn(Optional.of(booking));

        Booking result = service.getBookingById(id);

        assertEquals(booking, result);
    }

    @Test
    void getBookingById_shouldThrowBookingNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        BookingNotFoundException thrown = assertThrows(
            BookingNotFoundException.class,
            () -> service.getBookingById(id)
        );

        assertTrue(thrown.getMessage().contains(id.toString()));
    }
}
