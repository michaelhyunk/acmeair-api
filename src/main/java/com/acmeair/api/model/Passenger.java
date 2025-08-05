package com.acmeair.api.model;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class Passenger {
    private UUID id;
    private String firstName;
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String passengerNote;

    public Passenger() {}

    public Passenger(UUID id, String firstName, String lastName, String email, String passengerNote) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passengerNote = passengerNote;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassengerNote() {
        return passengerNote;
    }

    public void setPassengerNote(String passengerNote) {
        this.passengerNote = passengerNote;
    }
}
