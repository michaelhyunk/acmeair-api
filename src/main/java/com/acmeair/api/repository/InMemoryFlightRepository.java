package com.acmeair.api.repository;

import com.acmeair.api.model.Flight;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFlightRepository implements FlightRepository {
    private final Map<UUID, Flight> flights = new ConcurrentHashMap<>();
    
    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }

    public Optional<Flight> findById(UUID id) {
        return Optional.ofNullable(flights.get(id));
    }

    public void save(Flight flight) {
        flights.put(flight.getId(), flight);
    }

    public void saveAll(List<Flight> flightList) {
        for (Flight flight: flightList) {
            save(flight);
        }
    }

    public void clear() {
        flights.clear();
    }
}
