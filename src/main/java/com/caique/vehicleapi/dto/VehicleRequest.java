package com.caique.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Vehicle creation request")
public record VehicleRequest(
        @Schema(example = "Toyota", required = true)
        @NotBlank(message = "Brand is required")
        String brand,

        @Schema(example = "Corolla", required = true)
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
        Double price,

        @Schema(example = "ABC1D23")
        @NotBlank(message = "Plate is required")
        String plate
) {
}
