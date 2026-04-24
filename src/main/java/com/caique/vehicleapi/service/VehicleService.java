package com.caique.vehicleapi.service;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
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
    public List<VehicleResponse> getAll(){
        return repository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // find by ID
    public VehicleResponse getById(Long id) {
        return toResponse(
                repository.findByIdAndActiveTrue(id)
                        .orElseThrow(() -> new RuntimeException("Vehicle not found"))
        );
    }

    // find with filters
    public List<VehicleResponse> getWithFilters(
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
                .map(this::toResponse)
                .toList();
    }

    // create vehicle
    public VehicleResponse create(VehicleRequest request) {

        Vehicle v = new Vehicle();
        v.setBrand(request.brand());
        v.setModel(request.model());
        v.setVehicleYear(request.vehicleYear());
        v.setColor(request.color());
        v.setPrice(request.price());
        v.setActive(true);

        return toResponse(repository.save(v));
    }

    // update vehicle
    public VehicleResponse update(Long id, VehicleRequest request) {

        Vehicle existing = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setBrand(request.brand());
        existing.setModel(request.model());
        existing.setVehicleYear(request.vehicleYear());
        existing.setColor(request.color());
        existing.setPrice(request.price());

        return toResponse(repository.save(existing));
    }

    // patch vehicle
    public VehicleResponse patch(Long id, VehicleRequest request) {

        Vehicle existing = repository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (request.brand() != null) existing.setBrand(request.brand());
        if (request.model() != null) existing.setModel(request.model());
        if (request.vehicleYear() != null) existing.setVehicleYear(request.vehicleYear());
        if (request.color() != null) existing.setColor(request.color());
        if (request.price() != null) existing.setPrice(request.price());

        return toResponse(repository.save(existing));
    }

    // soft delete
    public void delete(Long id) {

        Vehicle vehicle = repository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setActive(false);
        repository.save(vehicle);
    }

    public List<VehicleResponse> getDeleted() {
        return repository.findByActiveFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // aux method
    private VehicleResponse toResponse(Vehicle v) {
        return new VehicleResponse(
                v.getId(),
                v.getBrand(),
                v.getModel(),
                v.getVehicleYear(),
                v.getColor(),
                v.getPrice()
        );
    }
}
