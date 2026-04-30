package com.caique.vehicleapi.specification;

import com.caique.vehicleapi.model.Vehicle;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static Specification<Vehicle> withFilters(
            String brand,
            Integer year,
            String color,
            Double minPrice,
            Double maxPrice,
            String plate
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isTrue(root.get("active")));

            if (brand != null) {
                predicates.add(
                        cb.equal(cb.lower(root.get("brand")), brand.toLowerCase())
                );
            }

            if (year != null) {
                predicates.add(cb.equal(root.get("vehicleYear"), year));
            }

            if (color != null) {
                predicates.add(
                        cb.equal(cb.lower(root.get("color")), color.toLowerCase())
                );
            }

            if (plate != null) {
                predicates.add(
                        cb.equal(cb.lower(root.get("plate")), plate.toLowerCase())
                );
            }

            if (minPrice != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice)
                );
            }

            if (maxPrice != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
