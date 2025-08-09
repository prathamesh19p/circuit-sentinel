package com.p.circuitbreaker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * Represents a travel destination with all its details.
 * This model is used for storing and transferring travel destination information.
 */
public class TravelDestination {

    @NotBlank(message = "Destination ID is required")
    @Size(max = 50, message = "Destination ID must not exceed 50 characters")
    private String destinationId;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country name must not exceed 100 characters")
    private String country;

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Destination name must not exceed 200 characters")
    private String name;

    @Size(max = 100, message = "City name must not exceed 100 characters")
    private String city;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Size(max = 100, message = "Best season to visit must not exceed 100 characters")
    private String bestSeasonToVisit;

    @Size(max = 500, message = "Attractions must not exceed 500 characters")
    private String attractions;

    // Default constructor
    public TravelDestination() {}

    // All-args constructor
    public TravelDestination(String destinationId, String country, String name, String city, 
                           String description, String category, String bestSeasonToVisit, String attractions) {
        this.destinationId = destinationId;
        this.country = country;
        this.name = name;
        this.city = city;
        this.description = description;
        this.category = category;
        this.bestSeasonToVisit = bestSeasonToVisit;
        this.attractions = attractions;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String destinationId;
        private String country;
        private String name;
        private String city;
        private String description;
        private String category;
        private String bestSeasonToVisit;
        private String attractions;

        public Builder destinationId(String destinationId) {
            this.destinationId = destinationId;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder bestSeasonToVisit(String bestSeasonToVisit) {
            this.bestSeasonToVisit = bestSeasonToVisit;
            return this;
        }

        public Builder attractions(String attractions) {
            this.attractions = attractions;
            return this;
        }

        public TravelDestination build() {
            return new TravelDestination(destinationId, country, name, city, description, category, bestSeasonToVisit, attractions);
        }
    }

    // Getters and Setters
    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBestSeasonToVisit() {
        return bestSeasonToVisit;
    }

    public void setBestSeasonToVisit(String bestSeasonToVisit) {
        this.bestSeasonToVisit = bestSeasonToVisit;
    }

    public String getAttractions() {
        return attractions;
    }

    public void setAttractions(String attractions) {
        this.attractions = attractions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelDestination that = (TravelDestination) o;
        return Objects.equals(destinationId, that.destinationId) &&
               Objects.equals(country, that.country) &&
               Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationId, country, name);
    }

    @Override
    public String toString() {
        return "TravelDestination{" +
                "destinationId='" + destinationId + '\'' +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", bestSeasonToVisit='" + bestSeasonToVisit + '\'' +
                ", attractions='" + attractions + '\'' +
                '}';
    }
}