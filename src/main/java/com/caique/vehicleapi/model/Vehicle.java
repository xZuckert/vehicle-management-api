package com.caique.vehicleapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private Integer vehicleYear;
    private String color;
    private Double price;
    // Vehicle.java

    @Column(unique = true, nullable = false)
    private String plate;

    @Schema(hidden = true)
    private Boolean active = true;

    // empty constructor
    public Vehicle() {
    }
}
