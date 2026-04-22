package com.caique.vehicleapi.repository;

import com.caique.vehicleapi.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByActiveTrue();
    Optional<Vehicle> findByIdAndActiveTrue(Long id);
    List<Vehicle> findByBrandAndActiveTrue(String marca);
}
