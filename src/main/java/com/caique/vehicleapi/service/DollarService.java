package com.caique.vehicleapi.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class DollarService {

    private final RestClient restClient;

    public DollarService() {
        this.restClient = RestClient.create();
    }

    @Cacheable(value = "dollar-rate")
    public Double getDollarRate() {

        Map response = restClient.get()
                .uri("https://economia.awesomeapi.com.br/json/last/USD-BRL")
                .retrieve()
                .body(Map.class);

        Map usdbrl = (Map) response.get("USDBRL");

        return Double.parseDouble(
                usdbrl.get("bid").toString()
        );
    }
}