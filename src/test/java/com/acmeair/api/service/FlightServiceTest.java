package com.acmeair.api.service;

import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceTest {
    private FlightRepository repository;
    private FlightService service;

    @BeforeEach
    void setup() {
        repository = mock(FlightRepository.class);
        service = new FlightService(repository);
    }

    @Test
    void getAllFlights_shouldReturnAllFlights() {
        UUID id = UUID.randomUUID();
        List<Flight> flights = List.of(
            new Flight(
                id,
                "NZ101",
                100,
                20,
                "AKL",
                "WLG",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
            )
        );

        when(repository.findAll()).thenReturn(flights);

        List<Flight> result = service.getAllFlights();

        assertEquals(1, result.size());
        assertEquals("AKL", result.get(0).origin());
    }

    @Test
    void getFlightsById_shouldReturnFlightById() {
        UUID id = UUID.randomUUID();
        Flight flight = new Flight(
            id,
            "NZ101",
            100,
            20,
            "AKL",
            "CHC",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1)
        );

        when(repository.findById(id)).thenReturn(Optional.of(flight));

        Flight result = service.getFlightById(id);

        assertEquals("CHC", result.destination());
        assertEquals(id, result.id());
    }

    @Test
    void getFlightById_shouldThrowWhenFlightNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        FlightNotFoundException thrown = assertThrows(
                FlightNotFoundException.class,
                () -> service.getFlightById(id)
        );

        assertTrue(thrown.getMessage().contains(id.toString()));
    }
}
