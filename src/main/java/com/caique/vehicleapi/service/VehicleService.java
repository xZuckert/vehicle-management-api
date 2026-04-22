package com.caique.vehicleapi.service;

import com.caique.vehicleapi.model.Vehicle;
import com.caique.vehicleapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository){
        this.repository = repository;
    }
    
    // find all
    public List<Vehicle> getAll(){
        return repository.findByActiveTrue();
    }

    // find by ID
    public Vehicle getById(Long id) {
        return repository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Vehivle not found"));
    }

    // find by brand
    public List<Vehicle> getByBrand(String brand) {
        return repository.findByBrandAndActiveTrue(brand);
    }

    // create vehicle
    public Vehicle create(Vehicle vehicle) {
        vehicle.setId(null);
        vehicle.setActive(true);
        return repository.save(vehicle);
    }

    // update vehicle
    public Vehicle update(Long id, Vehicle updated) {
        Vehicle existing = getById(id);
        // brand, model, year, color, price
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setVehicleYear(existing.getVehicleYear());
        existing.setColor(existing.getColor());
        existing.setPrice(existing.getPrice());

        return repository.save(existing);
    }

    // soft delete
    public void delete(Long id) {
        Vehicle vehicle = getById(id);
        vehicle.setActive(false);
        repository.save(vehicle);
    }
}
