package com.caique.vehicleapi.dto;

public record VehicleResponse(
        Long id,
        String brand,
        String model,
        Integer vehicleYear,
        String color,
        Double price
) {
}
