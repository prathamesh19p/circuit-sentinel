package com.p.circuitbreaker.controller;

import com.p.circuitbreaker.dto.ApiResponse;
import com.p.circuitbreaker.dto.DestinationRequest;
import com.p.circuitbreaker.model.TravelDestination;
import com.p.circuitbreaker.service.TravelDestinationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for travel destination operations.
 */
@RestController
@RequestMapping("/api/v1/destinations")
@Validated
public class DestinationController {

    private static final Logger log = LoggerFactory.getLogger(DestinationController.class);
    private final TravelDestinationService travelDestinationService;

    public DestinationController(TravelDestinationService travelDestinationService) {
        this.travelDestinationService = travelDestinationService;
    }

    /**
     * Get destination details by location and country.
     *
     * @param location the destination location
     * @param country  the country
     * @return destination details
     */
    @GetMapping("/details")
    public ResponseEntity<ApiResponse<TravelDestination>> getDestinationDetails(
            @RequestParam @NotBlank(message = "Location is required") String location,
            @RequestParam @NotBlank(message = "Country is required") String country) {
        
        log.info("Received request for destination details - location: {}, country: {}", location, country);
        
        try {
            TravelDestination destination = travelDestinationService.getDestinationDetails(location, country);
            ApiResponse<TravelDestination> response = ApiResponse.success("Destination details retrieved successfully", destination);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving destination details for location: {} and country: {}", location, country, e);
            ApiResponse<TravelDestination> response = ApiResponse.error("Failed to retrieve destination details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get attractions for a destination.
     *
     * @param location the destination location
     * @param country  the country
     * @return attractions information
     */
    @GetMapping("/attractions")
    public ResponseEntity<ApiResponse<String>> getAttractions(
            @RequestParam @NotBlank(message = "Location is required") String location,
            @RequestParam @NotBlank(message = "Country is required") String country) {
        
        log.info("Received request for attractions - location: {}, country: {}", location, country);
        
        try {
            String attractions = travelDestinationService.getAttractions(location, country);
            ApiResponse<String> response = ApiResponse.success("Attractions retrieved successfully", attractions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving attractions for location: {} and country: {}", location, country, e);
            ApiResponse<String> response = ApiResponse.error("Failed to retrieve attractions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get destination details using request body.
     *
     * @param request the destination request
     * @return destination details
     */
    @PostMapping("/details")
    public ResponseEntity<ApiResponse<TravelDestination>> getDestinationDetailsPost(
            @Valid @RequestBody DestinationRequest request) {
        
        log.info("Received POST request for destination details - location: {}, country: {}", 
                request.getLocation(), request.getCountry());
        
        try {
            TravelDestination destination = travelDestinationService.getDestinationDetails(
                    request.getLocation(), request.getCountry());
            ApiResponse<TravelDestination> response = ApiResponse.success("Destination details retrieved successfully", destination);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving destination details for request: {}", request, e);
            ApiResponse<TravelDestination> response = ApiResponse.error("Failed to retrieve destination details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Health check endpoint.
     *
     * @return health status
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", "OK"));
    }
}
