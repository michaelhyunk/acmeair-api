package com.acmeair.api.controller;

import com.acmeair.api.dto.flight.FlightResponseDto;
import com.acmeair.api.exception.FlightNotFoundException;
import com.acmeair.api.mapper.FlightMapper;
import com.acmeair.api.model.Flight;
import com.acmeair.api.service.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @MockBean
    private FlightMapper flightMapper;

    private UUID flightId;
    private Flight flight;
    private FlightResponseDto dto;

    @BeforeEach
    void setup() {
        flightId = UUID.randomUUID();
        flight = new Flight(
            flightId,
            "NZ101",
            100,
            "AKL",
            "WLG",
            LocalDateTime.of(2025, 8, 10, 10, 0),
            LocalDateTime.of(2025, 8, 10, 13, 0)
        );

        dto = new FlightResponseDto();
        dto.setFlightId(flightId);
        dto.setFlightNumber("NZ101");
        dto.setTotalSeats(100);

        when(flightMapper.toDto(flight)).thenReturn(dto);
    }

    @Test
    void shouldReturnAllFlights() throws Exception {
        when(flightService.getAllFlights()).thenReturn(List.of(flight));

        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].flightId").value(flightId.toString()))
                .andExpect(jsonPath("$[0].flightNumber").value("NZ101"))
                .andExpect(jsonPath("$[0].totalSeats").value(100));
    }

    @Test
    void shouldReturnFlightById() throws Exception {
        when(flightService.getFlightById(flightId)).thenReturn(flight);

        mockMvc.perform(get("/flights/" + flightId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.flightId").value(flightId.toString()))
            .andExpect(jsonPath("$.flightNumber").value("NZ101"))
            .andExpect(jsonPath("$.totalSeats").value(100));
    }

    @Test
    void shouldReturn404IfFLightNotFound() throws Exception {
        UUID missingId = UUID.randomUUID();

        when(flightService.getFlightById(missingId))
                .thenThrow(new FlightNotFoundException(missingId));

        mockMvc.perform(get("/flights/" + missingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenUUIDIsNotProvided() throws Exception {
        mockMvc.perform(get("/flights/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }
}
