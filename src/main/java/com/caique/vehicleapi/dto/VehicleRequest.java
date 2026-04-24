package com.caique.vehicleapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleRequest(
        @NotBlank(message = "Brand is required")
        String brand,

        @NotBlank(message = "Model is required")
        String model,

        @NotNull(message = "Year is required")
        Integer vehicleYear,

        @NotBlank(message = "Color is required")
        String color,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price
) {
}
