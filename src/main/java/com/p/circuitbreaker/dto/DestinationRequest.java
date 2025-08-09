package com.p.circuitbreaker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for handling destination requests.
 */
public class DestinationRequest {

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    public DestinationRequest() {}

    public DestinationRequest(String location, String country) {
        this.location = location;
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "DestinationRequest{" +
                "location='" + location + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
