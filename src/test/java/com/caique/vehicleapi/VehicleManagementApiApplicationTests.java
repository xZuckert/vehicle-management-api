package com.caique.vehicleapi;

import com.caique.vehicleapi.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class VehicleManagementApiApplicationTests {

    @Autowired
    private VehicleService service;

    @Test
    void contextLoads() {
        assertNotNull(service);
    }
}
