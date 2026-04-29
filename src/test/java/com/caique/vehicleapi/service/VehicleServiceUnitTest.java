package com.caique.vehicleapi.service;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
import com.caique.vehicleapi.exception.ConflictException;
import com.caique.vehicleapi.model.Vehicle;
import com.caique.vehicleapi.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceUnitTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private VehicleService service;

    @Test
    void shouldCreateVehicle() {

        VehicleRequest request = new VehicleRequest(
                "Fiat",
                "Stilo",
                2008,
                "Gray",
                25000.0,
                "ABC1D23"
        );

        Vehicle saved = new Vehicle();
        saved.setId(1L);
        saved.setBrand("Fiat");
        saved.setModel("Stilo");

        when(repository.save(any())).thenReturn(saved);

        VehicleResponse response = service.create(request);

        assertNotNull(response);
        assertEquals("Fiat", response.brand());

        verify(repository).save(any());
    }

    @Test
    void shouldFailWhenPlateAlreadyExists() {

        VehicleRequest request = new VehicleRequest(
                "Fiat", "Uno", 2010, "Black", 20000.0, "ABC1D23"
        );

        when(repository.existsByPlate("ABC1D23")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(request));

        verify(repository, never()).save(any());
    }
}