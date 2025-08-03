package com.acmeair.api.repository;

import com.acmeair.api.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookingRepository {
    private final Map<UUID, Booking> bookings = new ConcurrentHashMap<>();

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    public Optional<Booking> findById(UUID id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public void save(Booking booking) {
        bookings.put(booking.id(), booking);
    }

    public void clear() {
        bookings.clear();
    }

    // TODO: Create for findby; passenger & flight. Also create status updates like cancel & confirm/creation
}
