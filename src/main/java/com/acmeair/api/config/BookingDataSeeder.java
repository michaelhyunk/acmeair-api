package com.acmeair.api.config;

import com.acmeair.api.model.Booking;
import com.acmeair.api.model.BookingStatus;
import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.BookingRepository;
import com.acmeair.api.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class BookingDataSeeder {

    @Bean
    CommandLineRunner seedBookings(BookingRepository bookingRepository, FlightRepository flightRepository) {
        return args -> {
            // Create and save sample flights
            UUID flight1Id = UUID.randomUUID();
            UUID flight2Id = UUID.randomUUID();

            Flight flight1 = new Flight(
                flight1Id,
                "NZ101",
                100,
                "AKL",
                "CHC",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
            );

            Flight flight2 = new Flight(
                flight2Id,
                "NZ202",
                80,
                "WLG",
                "AKL",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1)
            );

            flightRepository.save(flight1);
            flightRepository.save(flight2);

            // Create and save sample bookings
            Booking booking1 = new Booking(
                UUID.randomUUID(),
                flight1Id,
                UUID.randomUUID(), // passengerId
                BookingStatus.CONFIRMED
            );

            Booking booking2 = new Booking(
                UUID.randomUUID(),
                flight2Id,
                UUID.randomUUID(),
                BookingStatus.CONFIRMED
            );

            bookingRepository.save(booking1);
            bookingRepository.save(booking2);
        };
    }
}
