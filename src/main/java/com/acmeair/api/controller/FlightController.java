package com.acmeair.api.controller;

import com.acmeair.api.dto.flight.FlightResponseDto;
import com.acmeair.api.mapper.FlightMapper;
import com.acmeair.api.model.Flight;
import com.acmeair.api.service.FlightService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger log = LoggerFactory.getLogger(FlightController.class);

    public FlightController(FlightService service, FlightMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        log.info("GET /flights called");
        List<Flight> flights = service.getAllFlights();

        log.debug("Returning {} flights", flights.size());
        List<FlightResponseDto> result = flights.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable("id") UUID id) {
        log.info("GET /flights/{id called"); // redacted in prod
        log.debug("Flight lookup request for ID: {}", id);
        Flight flight = service.getFlightById(id);
        FlightResponseDto result = mapper.toDto(flight);
        return ResponseEntity.ok(result);
    }
    
}
 