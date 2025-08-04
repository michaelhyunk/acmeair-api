package com.acmeair.api.repository;

import com.acmeair.api.model.Booking;

import java.util.*;

public interface BookingRepository {
    List<Booking> findAll();
    Optional<Booking> findById(UUID id);
    void save(Booking booking);
}
