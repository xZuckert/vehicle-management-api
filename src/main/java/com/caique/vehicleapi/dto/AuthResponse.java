package com.caique.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(example = "****************************************************************************************" +
                "*************************************************************************", required = true)
        String token,
        @Schema(example = "Bearer", required = true)
        String type
) {}