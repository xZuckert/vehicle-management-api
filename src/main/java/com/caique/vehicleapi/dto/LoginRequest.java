package com.caique.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(example = "admin", required = true)
        @NotBlank(message = "Username is required")
        String username,

        @Schema(example = "123", required = true)
        @NotBlank(message = "Password is required")
        String password

) {}