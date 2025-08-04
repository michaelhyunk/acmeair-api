package com.acmeair.api.service;

import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.FlightRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository repository;
    
    public FlightService(FlightRepository repository) {
        this.repository = repository;
    }

    public List<Flight> getAllFlights() {
        return repository.findAll();
    }

    public Flight getFlightById(UUID id) {
        Optional<Flight> result = repository.findById(id);

        if (result.isEmpty()) {
            throw new FlightNotFoundException(id);
        }

        return result.get();
    }
}
