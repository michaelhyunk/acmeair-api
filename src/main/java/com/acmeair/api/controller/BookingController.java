package com.acmeair.api.controller;

import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.dto.booking.BookingResponseDto;
import com.acmeair.api.mapper.BookingMapper;
import com.acmeair.api.model.Booking;
import com.acmeair.api.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService service;
    private final BookingMapper mapper;

    public BookingController(BookingService service, BookingMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<Booking> bookings = service.getAllBookings();
        List<BookingResponseDto> result = bookings.stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable("id") UUID id) {
        Booking booking = service.getBookingById(id);
        BookingResponseDto result = mapper.toDto(booking);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto requestDto) {
        Booking newBooking = service.createBooking(requestDto);
        BookingResponseDto result = mapper.toDto(newBooking);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable("id") UUID id, @RequestBody BookingRequestDto requestDto) {
        Booking updatedBooking = service.updateBooking(id, requestDto);
        BookingResponseDto result = mapper.toDto(updatedBooking);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable("id") UUID id) {        
        service.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
