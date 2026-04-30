package com.caique.vehicleapi.repository;

import com.caique.vehicleapi.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends
        JpaRepository<Vehicle, Long>,
        JpaSpecificationExecutor<Vehicle> {

    List<Vehicle> findByActiveTrue();
    Optional<Vehicle> findByIdAndActiveTrue(Long id);
    List<Vehicle> findByActiveFalse();
    Optional<Vehicle> findByIdAndActiveFalse(Long id);
    boolean existsByPlate(String plate);
}
