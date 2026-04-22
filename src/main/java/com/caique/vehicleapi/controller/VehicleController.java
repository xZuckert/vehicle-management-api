package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.model.Vehicle;
import com.caique.vehicleapi.service.VehicleService;
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
    public List<Vehicle> getAll(@RequestParam(required = false) String brand) {
        if (brand != null) {
            return service.getByBrand(brand); // filter by brand param
        }
        return service.getAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // POST
    @PostMapping
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return service.create(vehicle);
    }

    // PUT
    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return service.update(id, vehicle);
    }

    // soft DELETE
    @DeleteMapping
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
