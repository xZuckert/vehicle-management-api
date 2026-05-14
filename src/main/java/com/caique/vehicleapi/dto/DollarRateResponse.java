package com.caique.vehicleapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DollarRateResponse(

        @JsonProperty("bid")
        String bid

) {
}