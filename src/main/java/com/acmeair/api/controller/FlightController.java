package com.acmeair.api.controller;

import com.acmeair.api.dto.flight.FlightResponseDto;
import com.acmeair.api.mapper.FlightMapper;
import com.acmeair.api.model.Flight;
import com.acmeair.api.service.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService service;
    private final FlightMapper mapper;

    public FlightController(FlightService service, FlightMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping()
    public List<FlightResponseDto> getAllFlights() {
        List<Flight> flights = service.getAllFlights();
        return flights.stream()
            .map(mapper::toDto)
            .toList();
    }

    @GetMapping("/{id}")
    public FlightResponseDto getFlightById(@PathVariable("id") UUID id) {
        Flight flight = service.getFlightById(id);
        return mapper.toDto(flight);
    }
    
}
