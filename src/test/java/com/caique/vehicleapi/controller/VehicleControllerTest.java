package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
import com.caique.vehicleapi.exception.NotFoundException;
import com.caique.vehicleapi.security.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.caique.vehicleapi.service.VehicleService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = VehicleController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtFilter.class
                )
        }
)
@ActiveProfiles("test")
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateVehicle() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "Fiat", "Stilo", 2008, "Gray", 25000.0, "ABC1D23"
        );

        when(service.create(request))
                .thenReturn(new VehicleResponse(
                        1L, "Fiat", "Stilo", 2008, "Gray", 25000.0, "ABC1D23"
                ));

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.brand").value("Fiat"));
    }

    @Test
    void shouldReturnBadRequest_whenInvalidData() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "", "", null, "", -10.0, "" // dados inválidos
        );

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFound_whenVehicleDoesNotExist() throws Exception {

        when(service.getById(1L))
                .thenThrow(new NotFoundException("Vehicle not found"));

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetVehicleById() throws Exception {

        when(service.getById(1L))
                .thenReturn(new VehicleResponse(
                        1L, "Fiat", "Stilo", 2008, "Gray", 25000.0, "ABC1D23"
                ));

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Fiat"));
    }

    @Test
    void shouldDeleteVehicle() throws Exception {

        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateVehicle() throws Exception {

        VehicleRequest request = new VehicleRequest(
                "Fiat", "Uno", 2008, "Black", 20000.0, "ABC1D23"
        );

        when(service.update(eq(1L), any()))
                .thenReturn(new VehicleResponse(
                        1L, "Fiat", "Uno", 2008, "Black", 20000.0, "ABC1D23"
                ));

        mockMvc.perform(put("/vehicles/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Uno"));
    }

    @Test
    void shouldListVehiclesWithFilters() throws Exception {

        when(service.getWithFilters(
                "Fiat", 2008, "Black",
                10000.0, 30000.0, "ABC1D23",
                0, 10, "id,asc"
        ))
                .thenReturn(new PageImpl<>(List.of(
                        new VehicleResponse(
                                1L, "Fiat", "Uno", 2008, "Black", 20000.0, "ABC1D23"
                        )
                )));

        mockMvc.perform(get("/vehicles")
                        .param("brand", "Fiat")
                        .param("year", "2008")
                        .param("color", "Black")
                        .param("minPrice", "10000")
                        .param("maxPrice", "30000")
                        .param("plate", "ABC1D23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].brand").value("Fiat"));
    }
}