package com.acmeair.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acmeair.api.model.Flight;
import com.acmeair.api.repository.FlightRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class FlightDataSeeder {
    @Bean
    CommandLineRunner seedFlights(FlightRepository flightRepository) {
        return args -> {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();

            flightRepository.save(
                new Flight(
                    id1,
                    "NZ101",
                    100,
                    40,
                    "AKL",
                    "WLG",
                    LocalDateTime.now().plusHours(2),
                    LocalDateTime.now().plusHours(5))
                );

            flightRepository.save(
                new Flight(
                    id2, 
                    "NZ202",
                    120,
                    20,
                    "CHC",
                    "AKL",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(2))
                );
        };
    }
}
