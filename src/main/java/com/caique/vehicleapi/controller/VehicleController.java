package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
import com.caique.vehicleapi.model.Vehicle;
import com.caique.vehicleapi.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // GET all
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<VehicleResponse> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return service.getWithFilters(brand, year, color, minPrice, maxPrice);
    }

    // Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public VehicleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // POST
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse create(@RequestBody @Valid VehicleRequest request) {
        return service.create(request);
    }

    // PUT
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse update(@PathVariable Long id, @RequestBody @Valid VehicleRequest request) {
        return service.update(id, request);
    }

    // PATCH
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse patch(@PathVariable Long id, @RequestBody VehicleRequest request) {
        return service.patch(id, request);
    }

    // soft DELETE)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // see deleted data using GET {{base_url}}/vehicles/deleted
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public List<VehicleResponse> getDeleted() {
        return service.getDeleted();
    }

    // see deleted data using ID
    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse getDeletedById(@PathVariable Long id) {
        return service.getDeletedById(id);
    }
}
