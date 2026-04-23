package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.model.Vehicle;
import com.caique.vehicleapi.service.VehicleService;
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
    public List<Vehicle> getAll(@RequestParam(required = false) String brand) {
        if (brand != null) {
            return service.getByBrand(brand); // filter by brand param
        }
        return service.getAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Vehicle getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // POST
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return service.create(vehicle);
    }

    // PUT
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return service.update(id, vehicle);
    }

    // PATCH
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle patch(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return service.patch(id, vehicle);
    }

    // soft DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // see deleted data using GET {{base_url}}/vehicles/deleted
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Vehicle> getDeleted() {
        return service.getDeleted();
    }
}
