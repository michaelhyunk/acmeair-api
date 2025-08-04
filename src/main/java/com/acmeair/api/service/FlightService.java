package com.acmeair.api.service;

import com.acmeair.api.model.Flight;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlightService {
    public List<Flight> getAllFlights() {
        return List.of();
    }

    public Flight getFlightById(UUID id) {
        return null;
    }
}
