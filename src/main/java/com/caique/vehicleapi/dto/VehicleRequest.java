package com.caique.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleRequest(
        @Schema(example = "Toyota")
        @NotBlank(message = "Brand is required")
        String brand,

        @Schema(example = "Corolla")
        @NotBlank(message = "Model is required")
        String model,

        @Schema(example = "2022")
        @NotNull(message = "Year is required")
        Integer vehicleYear,

        @Schema(example = "Polar White")
        @NotBlank(message = "Color is required")
        String color,

        @Schema(example = "130000")
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price
) {
}
