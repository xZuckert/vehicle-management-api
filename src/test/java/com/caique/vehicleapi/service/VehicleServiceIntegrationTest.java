package com.caique.vehicleapi.service;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VehicleServiceIntegrationTest {

    @Autowired
    private VehicleService service;

    @Test
    void shouldCreateVehicle() {

        VehicleRequest request = new VehicleRequest(
                "Fiat",
                "Stilo",
                2008,
                "Scandium Gray",
                25000.0
        );

        VehicleResponse response = service.create(request);

        assertNotNull(response.id());
        assertEquals("Fiat", response.brand());
    }
}


