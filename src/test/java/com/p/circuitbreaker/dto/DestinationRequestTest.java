package com.p.circuitbreaker.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DestinationRequest DTO Tests")
class DestinationRequestTest {

    @Test
    @DisplayName("Should create DestinationRequest with constructor")
    void shouldCreateDestinationRequestWithConstructor() {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";

        // When
        DestinationRequest request = new DestinationRequest(location, country);

        // Then
        assertNotNull(request);
        assertEquals(location, request.getLocation());
        assertEquals(country, request.getCountry());
    }

    @Test
    @DisplayName("Should create empty DestinationRequest")
    void shouldCreateEmptyDestinationRequest() {
        // When
        DestinationRequest request = new DestinationRequest();

        // Then
        assertNotNull(request);
        assertNull(request.getLocation());
        assertNull(request.getCountry());
    }

    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetProperties() {
        // Given
        DestinationRequest request = new DestinationRequest();

        // When
        request.setLocation("Eiffel Tower");
        request.setCountry("France");

        // Then
        assertEquals("Eiffel Tower", request.getLocation());
        assertEquals("France", request.getCountry());
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void shouldReturnCorrectToString() {
        // Given
        DestinationRequest request = new DestinationRequest("Mount Fuji", "Japan");

        // When
        String toString = request.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("location='Mount Fuji'"));
        assertTrue(toString.contains("country='Japan'"));
    }

    @Test
    @DisplayName("Should handle null values in toString")
    void shouldHandleNullValuesInToString() {
        // Given
        DestinationRequest request = new DestinationRequest();

        // When
        String toString = request.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("location='null'"));
        assertTrue(toString.contains("country='null'"));
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        // Given
        DestinationRequest request = new DestinationRequest();

        // When
        request.setLocation("");
        request.setCountry("");

        // Then
        assertEquals("", request.getLocation());
        assertEquals("", request.getCountry());
    }
}
