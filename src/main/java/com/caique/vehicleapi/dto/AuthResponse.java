package com.caique.vehicleapi.dto;

public record AuthResponse(
        String token,
        String type
) {}