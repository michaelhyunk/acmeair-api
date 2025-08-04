package com.acmeair.api.repository;

import com.acmeair.api.model.Booking;

import java.util.*;

public interface BookingRepository {
    List<Booking> findAll();
    Optional<Booking> findById(UUID id);
    Booking save(Booking booking);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
