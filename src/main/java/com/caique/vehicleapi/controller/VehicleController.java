package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.dto.VehicleResponse;
import com.caique.vehicleapi.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    @Operation(summary = "List vehicles", description = "Returns all active vehicles with optional filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<VehicleResponse> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String plate
    ) {
        return service.getWithFilters(brand, year, color, minPrice, maxPrice, plate);
    }

    // Get by ID
    @Operation(summary = "Get vehicle by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public VehicleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // POST
    @Operation(summary = "Create a new vehicle (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleResponse> create(@RequestBody @Valid VehicleRequest request) {

        VehicleResponse response = service.create(request);

        URI location = URI.create("/vehicles/" + response.id());

        return ResponseEntity.created(location).body(response);
    }
    // PUT
    @Operation(summary = "Update vehicle completely (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse update(@PathVariable Long id, @RequestBody @Valid VehicleRequest request) {
        return service.update(id, request);
    }

    // PATCH
    @Operation(summary = "Partially update vehicle (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse patch(@PathVariable Long id, @RequestBody VehicleRequest request) {
        return service.patch(id, request);
    }

    // soft DELETE
    @Operation(summary = "Soft delete vehicle (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // see deleted data using GET {{base_url}}/vehicles/deleted
    @Operation(summary = "Get all deleted vehicles (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public List<VehicleResponse> getDeleted() {
        return service.getDeleted();
    }

    // see deleted data using ID
    @Operation(summary = "Get deleted vehicle by ID (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse getDeletedById(@PathVariable Long id) {
        return service.getDeletedById(id);
    }
}
