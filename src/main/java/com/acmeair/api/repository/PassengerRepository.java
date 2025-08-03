package com.acmeair.api.repository;

import com.acmeair.api.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PassengerRepository {
    private final Map<UUID, Passenger> passengers = new ConcurrentHashMap<>();

    public List<Passenger> findAll() {
        return new ArrayList<>(passengers.values());
    }

    public Optional<Passenger> findById(UUID id) {
        return Optional.ofNullable(passengers.get(id));
    }

    public void save(Passenger passenger) {
        passengers.put(passenger.id(), passenger);
    }

    public void clear() {
        passengers.clear();
    }
}
