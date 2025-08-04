package com.acmeair.api.repository;

import com.acmeair.api.model.Flight;

import java.util.*;

public interface FlightRepository {
    List<Flight> findAll();
    Optional<Flight> findById(UUID id);
    void save(Flight flight);
}
