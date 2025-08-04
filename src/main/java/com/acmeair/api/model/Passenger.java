package com.acmeair.api.model;

import java.util.UUID;

/**
*  Naturally thought I'd need this, but eventually decided to keep things light.
*  Am anticipating this will become useful in the near future, and bind Passenger with Booking and Flight
*  Decided not to do that just yet, but leave this as is to reflect my intent and design consideration
**/

public class Passenger {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    public Passenger() {}

    public Passenger(UUID id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
