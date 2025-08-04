package com.acmeair.api.service;

import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.FlightRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlightService {

    private final FlightRepository repository;
    
    private static final Logger log = LoggerFactory.getLogger(FlightService.class);
    
    public FlightService(FlightRepository repository) {
        this.repository = repository;
    }

    public List<Flight> getAllFlights() {
        log.info("Fetching all flights");
        return repository.findAll();
    }

    public Flight getFlightById(UUID id) {
        log.debug("Looking up flight by ID: {}", id);
        Optional<Flight> result = repository.findById(id);

        if (result.isEmpty()) {
            log.debug("Flight not found for ID: {}", id);
            log.warn("Flight not found");
            throw new FlightNotFoundException(id);
        }

        return result.get();
    }
}
