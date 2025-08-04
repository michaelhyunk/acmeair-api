package com.acmeair.api.repository;

import com.acmeair.api.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookingRepository implements BookingRepository {
    private final Map<UUID, Booking> bookings = new ConcurrentHashMap<>();

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    public Optional<Booking> findById(UUID id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public Booking save(Booking booking) {
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public void deleteById(UUID id) {
        bookings.remove(id);
    }

    public boolean existsById(UUID id) {
        return bookings.containsKey(id);
    }

    // TODO: Create for findby; passenger & flight. Also create status updates like cancel & confirm/creation
}
