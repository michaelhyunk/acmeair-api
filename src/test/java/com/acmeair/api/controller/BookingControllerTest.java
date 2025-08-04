package com.acmeair.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.dto.booking.BookingResponseDto;
import com.acmeair.api.exception.BookingNotFoundException;
import com.acmeair.api.mapper.BookingMapper;
import com.acmeair.api.model.Booking;
import com.acmeair.api.model.BookingStatus;
import com.acmeair.api.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingMapper bookingMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID bookingId;
    private UUID flightId;
    private UUID passengerId;

    private Booking booking;
    private BookingRequestDto requestDto;
    private BookingResponseDto responseDto;

    @BeforeEach
    void setup() {
        bookingId = UUID.randomUUID();
        flightId = UUID.randomUUID();
        passengerId = UUID.randomUUID();

        booking = new Booking(bookingId, flightId, passengerId, BookingStatus.CONFIRMED);

        requestDto = new BookingRequestDto(passengerId);
        requestDto.setFlightId(flightId);

        responseDto = new BookingResponseDto();
        responseDto.setBookingId(bookingId);
        responseDto.setFlightId(flightId);
        responseDto.setPassengerId(passengerId);
        responseDto.setStatus(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldReturnAllBooking() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(responseDto); 

        mockMvc.perform(get("/bookings"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].bookingId").value(bookingId.toString()))
            .andExpect(jsonPath("$[0].flightId").value(flightId.toString()))
            .andExpect(jsonPath("$[0].passengerId").value(passengerId.toString()))
            .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void shouldReturnBookingById() throws Exception {
        when(bookingService.getBookingById(bookingId)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(responseDto); 

        mockMvc.perform(get("/bookings/" + bookingId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookingId").value(bookingId.toString()))
            .andExpect(jsonPath("$.flightId").value(flightId.toString()))
            .andExpect(jsonPath("$.passengerId").value(passengerId.toString()))
            .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldReturn404IfBookingNotFound() throws Exception {
        UUID missingId = UUID.randomUUID();

        when(bookingService.getBookingById(missingId)).thenThrow(new BookingNotFoundException(missingId));

        mockMvc.perform(get("/bookings/" + missingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenUUIDIsInvalid() throws Exception {
        mockMvc.perform(get("/bookings/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateBookingSuccessfully() throws Exception {
        when(bookingMapper.toModel(any())).thenReturn(booking);
        when(bookingService.createBooking(requestDto)).thenReturn(booking);
        when(bookingMapper.toDto(any())).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(bookingId.toString()));
    }

    @Test
    void shouldUpdateBookingSuccessfully() throws Exception {
        when(bookingMapper.toModel(any())).thenReturn(booking);
        when(bookingService.updateBooking(any(), any())).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(responseDto);

        mockMvc.perform(put("/bookings/" + bookingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(bookingId.toString()));
    }

    @Test
    void shouldReturn404WhenUpdatingMissingBooking() throws Exception {
        when(bookingMapper.toModel(any())).thenReturn(booking);
        when(bookingService.updateBooking(any(), any()))
                .thenThrow(new BookingNotFoundException(bookingId));

        mockMvc.perform(put("/bookings/" + bookingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCancelBookingSuccessfully() throws Exception {
        mockMvc.perform(delete("/bookings/" + bookingId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenCancellingMissingBooking() throws Exception {
        doThrow(new BookingNotFoundException(bookingId))
            .when(bookingService).cancelBooking(bookingId);

        mockMvc.perform(delete("/bookings/" + bookingId))
                .andExpect(status().isNotFound());
    }
}
