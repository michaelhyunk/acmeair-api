package com.acmeair.api.controller;

import com.acmeair.api.model.Flight;
import com.acmeair.api.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private FlightService flightService;

    @Test
    void shouldReturnAllFlights() throws Exception {
        UUID id = UUID.randomUUID();
        Flight sampleFlight = new Flight(
            id,
            "AKL",
            "WLG",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
        );

        when(flightService.getAllFlights()).thenReturn(List.of(sampleFlight));

        mockMvc.perform(get("/flights"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].origin").value("AKL"));
    }

    @Test
    void shouldReturnFlightById() throws Exception {
        UUID id = UUID.randomUUID();
        Flight flight = new Flight(
            id,
            "AKL",
            "CHC",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
        );

        when(flightService.getFlightById(id)).thenReturn(flight);
    }

    @Test
    void shouldReturn404IfFLightNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(flightService.getFlightById(id)).thenThrow(new NoSuchElementException("Flight not found"));

        mockMvc.perform(get("/flights/" + id)).andExpect(status().isNotFound());
    }
}
