package com.caique.vehicleapi.dto;

public record VehiclePriceUsdResponse(

        Long vehicleId,
        String brand,
        String model,
        Double priceBRL,
        java.math.BigDecimal priceUSD,
        Double usdRate

) {
}