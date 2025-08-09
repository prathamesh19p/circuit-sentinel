package com.p.circuitbreaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p.circuitbreaker.dto.ApiResponse;
import com.p.circuitbreaker.dto.DestinationRequest;
import com.p.circuitbreaker.model.TravelDestination;
import com.p.circuitbreaker.service.TravelDestinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Destination Controller Tests")
class DestinationControllerTest {

    @Mock
    private TravelDestinationService travelDestinationService;

    @InjectMocks
    private DestinationController destinationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(destinationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should successfully get destination details")
    void shouldSuccessfullyGetDestinationDetails() throws Exception {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";
        TravelDestination expectedDestination = TravelDestination.builder()
                .destinationId("DEST001")
                .name(location)
                .country(country)
                .city("Estes Park")
                .description("Beautiful national park")
                .category("National Park")
                .bestSeasonToVisit("Summer")
                .attractions("Trail Ridge Road, Bear Lake")
                .build();

        when(travelDestinationService.getDestinationDetails(location, country))
                .thenReturn(expectedDestination);

        // When
        ResponseEntity<ApiResponse<TravelDestination>> response = 
                destinationController.getDestinationDetails(location, country);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Destination details retrieved successfully", response.getBody().getMessage());
        assertEquals(expectedDestination, response.getBody().getData());

        verify(travelDestinationService).getDestinationDetails(location, country);
    }

    @Test
    @DisplayName("Should successfully get attractions")
    void shouldSuccessfullyGetAttractions() throws Exception {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";
        String expectedAttractions = "Rocky Mountain is renowned for its stunning alpine scenery";

        when(travelDestinationService.getAttractions(location, country))
                .thenReturn(expectedAttractions);

        // When
        ResponseEntity<ApiResponse<String>> response = 
                destinationController.getAttractions(location, country);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Attractions retrieved successfully", response.getBody().getMessage());
        assertEquals(expectedAttractions, response.getBody().getData());

        verify(travelDestinationService).getAttractions(location, country);
    }

    @Test
    @DisplayName("Should successfully get destination details via POST")
    void shouldSuccessfullyGetDestinationDetailsViaPost() throws Exception {
        // Given
        DestinationRequest request = new DestinationRequest("Rocky Mountain", "USA");
        TravelDestination expectedDestination = TravelDestination.builder()
                .destinationId("DEST001")
                .name("Rocky Mountain")
                .country("USA")
                .city("Estes Park")
                .description("Beautiful national park")
                .category("National Park")
                .bestSeasonToVisit("Summer")
                .attractions("Trail Ridge Road, Bear Lake")
                .build();

        when(travelDestinationService.getDestinationDetails(request.getLocation(), request.getCountry()))
                .thenReturn(expectedDestination);

        // When
        ResponseEntity<ApiResponse<TravelDestination>> response = 
                destinationController.getDestinationDetailsPost(request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Destination details retrieved successfully", response.getBody().getMessage());
        assertEquals(expectedDestination, response.getBody().getData());

        verify(travelDestinationService).getDestinationDetails(request.getLocation(), request.getCountry());
    }

    @Test
    @DisplayName("Should return health status")
    void shouldReturnHealthStatus() {
        // When
        ResponseEntity<ApiResponse<String>> response = destinationController.health();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Service is healthy", response.getBody().getMessage());
        assertEquals("OK", response.getBody().getData());
    }

    @Test
    @DisplayName("Should handle service exception in getDestinationDetails")
    void shouldHandleServiceExceptionInGetDestinationDetails() {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";
        RuntimeException exception = new RuntimeException("Service unavailable");

        when(travelDestinationService.getDestinationDetails(location, country))
                .thenThrow(exception);

        // When
        ResponseEntity<ApiResponse<TravelDestination>> response = 
                destinationController.getDestinationDetails(location, country);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Failed to retrieve destination details"));

        verify(travelDestinationService).getDestinationDetails(location, country);
    }

    @Test
    @DisplayName("Should handle service exception in getAttractions")
    void shouldHandleServiceExceptionInGetAttractions() {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";
        RuntimeException exception = new RuntimeException("Service unavailable");

        when(travelDestinationService.getAttractions(location, country))
                .thenThrow(exception);

        // When
        ResponseEntity<ApiResponse<String>> response = 
                destinationController.getAttractions(location, country);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Failed to retrieve attractions"));

        verify(travelDestinationService).getAttractions(location, country);
    }

    @Test
    @DisplayName("Should handle service exception in getDestinationDetailsPost")
    void shouldHandleServiceExceptionInGetDestinationDetailsPost() {
        // Given
        DestinationRequest request = new DestinationRequest("Rocky Mountain", "USA");
        RuntimeException exception = new RuntimeException("Service unavailable");

        when(travelDestinationService.getDestinationDetails(request.getLocation(), request.getCountry()))
                .thenThrow(exception);

        // When
        ResponseEntity<ApiResponse<TravelDestination>> response = 
                destinationController.getDestinationDetailsPost(request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Failed to retrieve destination details"));

        verify(travelDestinationService).getDestinationDetails(request.getLocation(), request.getCountry());
    }
}
