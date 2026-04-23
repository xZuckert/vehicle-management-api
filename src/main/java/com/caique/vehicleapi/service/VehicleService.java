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
        return repository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    // find with filters
    public List<Vehicle> getWithFilters(
            String brand,
            Integer year,
            String color,
            Double minPrice,
            Double maxPrice
    ) {
        return repository.findByActiveTrue().stream()
                .filter(v -> brand == null || v.getBrand().equalsIgnoreCase(brand))
                .filter(v -> year == null || (v.getVehicleYear() != null && v.getVehicleYear().equals(year)))
                .filter(v -> color == null || (v.getColor() != null && v.getColor().equalsIgnoreCase(color)))
                .filter(v -> minPrice == null || v.getPrice() >= minPrice)
                .filter(v -> maxPrice == null || v.getPrice() <= maxPrice)
                .toList();
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
        existing.setVehicleYear(updated.getVehicleYear());
        existing.setColor(updated.getColor());
        existing.setPrice(updated.getPrice());

        return repository.save(existing);
    }

    // patch vehicle
    public Vehicle patch(Long id, Vehicle updated) {

        Vehicle existing = getById(id);

        if (updated.getBrand() != null) {
            existing.setBrand(updated.getBrand());
        }
        if (updated.getModel() != null) {
            existing.setModel(updated.getModel());
        }
        if (updated.getVehicleYear() != null) {
            existing.setVehicleYear(updated.getVehicleYear());
        }
        if (updated.getColor() != null) {
            existing.setColor(updated.getColor());
        }
        if (updated.getPrice() != null) {
            existing.setPrice(updated.getPrice());
        }

        return repository.save(existing);
    }

    // soft delete
    public void delete(Long id) {
        Vehicle vehicle = getById(id);
        vehicle.setActive(false);
        repository.save(vehicle);
    }

    public List<Vehicle> getDeleted() {
        return repository.findByActiveFalse();
    }
}
