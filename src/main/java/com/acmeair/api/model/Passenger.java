package com.acmeair.api.model;

import java.util.UUID;

public record Passenger (
    UUID id,
    String name,
    String email
) {}
