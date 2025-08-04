package com.acmeair.api.service;

import com.acmeair.api.exception.BookingNotFoundException;
import com.acmeair.api.model.Booking;
import com.acmeair.api.repository.InMemoryBookingRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {
    private final InMemoryBookingRepository repository;

    public BookingService(InMemoryBookingRepository repository) {
        this.repository = repository;
    }

    public List<Booking> getAllBookings() {
        return repository.findAll();
    }

    public Booking getBookingById(UUID id) {
        Optional<Booking> result = repository.findById(id);

        if (result.isEmpty()) {
            throw new BookingNotFoundException(id);
        }

        return result.get();
    }
}
