package com.acmeair.api.controller;

import com.acmeair.api.dto.flight.FlightResponseDto;
import com.acmeair.api.mapper.FlightMapper;
import com.acmeair.api.model.Flight;
import com.acmeair.api.service.FlightService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService service;
    private final FlightMapper mapper;

    public FlightController(FlightService service, FlightMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        List<Flight> flights = service.getAllFlights();
        List<FlightResponseDto> result = flights.stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable("id") UUID id) {
        Flight flight = service.getFlightById(id);
        FlightResponseDto result = mapper.toDto(flight);
        return ResponseEntity.ok(result);
    }
    
}
 