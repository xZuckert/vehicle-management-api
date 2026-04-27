package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
import com.caique.vehicleapi.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehicles", description = "Vehicle management APIs")
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // GET all
    @Operation(summary = "Get all vehicles with optional filters")
    @ApiResponse(
            responseCode = "200",
            description = "List of vehicles",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleResponse.class)
            )
    )
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
    @Operation(summary = "Get vehicle by ID")
    @ApiResponse(responseCode = "200", description = "Vehicle found")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public VehicleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // POST
    @Operation(summary = "Create a new vehicle (ADMIN only)")
    @ApiResponse(responseCode = "201", description = "Vehicle created")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse create(@RequestBody @Valid VehicleRequest request) {
        return service.create(request);
    }

    // PUT
    @Operation(summary = "Update vehicle completely (ADMIN only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse update(@PathVariable Long id, @RequestBody @Valid VehicleRequest request) {
        return service.update(id, request);
    }

    // PATCH
    @Operation(summary = "Partially update vehicle (ADMIN only)")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse patch(@PathVariable Long id, @RequestBody VehicleRequest request) {
        return service.patch(id, request);
    }

    // soft DELETE)
    @Operation(summary = "Soft delete vehicle (ADMIN only)")
    @ApiResponse(responseCode = "204", description = "Vehicle deleted")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // see deleted data using GET {{base_url}}/vehicles/deleted
    @Operation(summary = "Get all deleted vehicles (ADMIN only)")
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public List<VehicleResponse> getDeleted() {
        return service.getDeleted();
    }

    // see deleted data using ID
    @Operation(summary = "Get deleted vehicle by ID (ADMIN only)")
    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse getDeletedById(@PathVariable Long id) {
        return service.getDeletedById(id);
    }
}
