package com.acmeair.api.controller;

import com.acmeair.api.model.Flight;
import com.acmeair.api.service.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Flight> getAllFlights() {
        return service.getAllFlights();
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable UUID id) {
        return service.getFlightById(id);
    }
    
}
