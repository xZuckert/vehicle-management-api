package com.caique.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record VehicleResponse(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Toyota")
        String brand,

        @Schema(example = "Corolla")
        String model,

        @Schema(example = "2022")
        Integer vehicleYear,

        @Schema(example = "Polar White")
        String color,

        @Schema(example = "130000")
        Double price
) {
}
