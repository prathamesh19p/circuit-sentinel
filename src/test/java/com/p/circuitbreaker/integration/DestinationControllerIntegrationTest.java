package com.p.circuitbreaker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p.circuitbreaker.dto.DestinationRequest;
import com.p.circuitbreaker.model.TravelDestination;
import com.p.circuitbreaker.service.TravelDestinationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("Destination Controller Integration Tests")
class DestinationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelDestinationService travelDestinationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return destination details successfully")
    void shouldReturnDestinationDetailsSuccessfully() throws Exception {
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

        // When & Then
        mockMvc.perform(get("/api/v1/destinations/details")
                        .param("location", location)
                        .param("country", country)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Destination details retrieved successfully"))
                .andExpect(jsonPath("$.data.destinationId").value("DEST001"))
                .andExpect(jsonPath("$.data.name").value(location))
                .andExpect(jsonPath("$.data.country").value(country))
                .andExpect(jsonPath("$.data.city").value("Estes Park"))
                .andExpect(jsonPath("$.data.description").value("Beautiful national park"))
                .andExpect(jsonPath("$.data.category").value("National Park"))
                .andExpect(jsonPath("$.data.bestSeasonToVisit").value("Summer"))
                .andExpect(jsonPath("$.data.attractions").value("Trail Ridge Road, Bear Lake"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should return attractions successfully")
    void shouldReturnAttractionsSuccessfully() throws Exception {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";
        String expectedAttractions = "Rocky Mountain is renowned for its stunning alpine scenery";

        when(travelDestinationService.getAttractions(location, country))
                .thenReturn(expectedAttractions);

        // When & Then
        mockMvc.perform(get("/api/v1/destinations/attractions")
                        .param("location", location)
                        .param("country", country)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Attractions retrieved successfully"))
                .andExpect(jsonPath("$.data").value(expectedAttractions))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should return destination details via POST successfully")
    void shouldReturnDestinationDetailsViaPostSuccessfully() throws Exception {
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

        // When & Then
        mockMvc.perform(post("/api/v1/destinations/details")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Destination details retrieved successfully"))
                .andExpect(jsonPath("$.data.destinationId").value("DEST001"))
                .andExpect(jsonPath("$.data.name").value("Rocky Mountain"))
                .andExpect(jsonPath("$.data.country").value("USA"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should return health status successfully")
    void shouldReturnHealthStatusSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/destinations/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Service is healthy"))
                .andExpect(jsonPath("$.data").value("OK"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should handle missing required parameters")
    void shouldHandleMissingRequiredParameters() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/destinations/details")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle service exception")
    void shouldHandleServiceException() throws Exception {
        // Given
        String location = "Rocky Mountain";
        String country = "USA";

        when(travelDestinationService.getDestinationDetails(location, country))
                .thenThrow(new RuntimeException("Service unavailable"));

        // When & Then
        mockMvc.perform(get("/api/v1/destinations/details")
                        .param("location", location)
                        .param("country", country)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Failed to retrieve destination details")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should handle invalid request body")
    void shouldHandleInvalidRequestBody() throws Exception {
        // Given
        String invalidJson = "{\"invalid\": \"json\"";

        // When & Then
        mockMvc.perform(post("/api/v1/destinations/details")
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
