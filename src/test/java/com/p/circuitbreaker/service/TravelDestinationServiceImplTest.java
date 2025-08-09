package com.p.circuitbreaker.service;

import com.p.circuitbreaker.model.TravelDestination;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TravelDestinationService Implementation Tests")
class TravelDestinationServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private RateLimiterRegistry rateLimiterRegistry;

    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Mock
    private RateLimiter rateLimiter;

    @Mock
    private CircuitBreaker circuitBreaker;

    private TravelDestinationServiceImpl travelDestinationService;

    @BeforeEach
    void setUp() {
        travelDestinationService = new TravelDestinationServiceImpl(webClient, rateLimiterRegistry, circuitBreakerRegistry);
    }

    @Test
    @DisplayName("Should successfully get destination details")
    void shouldSuccessfullyGetDestinationDetails() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = "USA";
        TravelDestination expectedDestination = TravelDestination.builder()
                .destinationId("DEST001")
                .name(destinationName)
                .country(country)
                .city("Estes Park")
                .description("Beautiful national park")
                .category("National Park")
                .bestSeasonToVisit("Summer")
                .attractions("Trail Ridge Road, Bear Lake")
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), eq(destinationName), eq(country))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TravelDestination.class)).thenReturn(Mono.just(expectedDestination));

        // When
        TravelDestination result = travelDestinationService.getDestinationDetails(destinationName, country);

        // Then
        assertNotNull(result);
        assertEquals(expectedDestination.getDestinationId(), result.getDestinationId());
        assertEquals(expectedDestination.getName(), result.getName());
        assertEquals(expectedDestination.getCountry(), result.getCountry());
        
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/travelDestination?destinationId={destinationId}&country={country}", destinationName, country);
        verify(requestHeadersUriSpec).retrieve();
        verify(responseSpec).bodyToMono(TravelDestination.class);
    }

    @Test
    @DisplayName("Should throw exception when destination name is null")
    void shouldThrowExceptionWhenDestinationNameIsNull() {
        // Given
        String destinationName = null;
        String country = "USA";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getDestinationDetails(destinationName, country);
        });
        assertEquals("Destination name is required and cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when country is null")
    void shouldThrowExceptionWhenCountryIsNull() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getDestinationDetails(destinationName, country);
        });
        assertEquals("Country is required and cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when destination name is empty")
    void shouldThrowExceptionWhenDestinationNameIsEmpty() {
        // Given
        String destinationName = "";
        String country = "USA";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getDestinationDetails(destinationName, country);
        });
        assertEquals("Destination name is required and cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should return fallback destination when external service fails")
    void shouldReturnFallbackDestinationWhenExternalServiceFails() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = "USA";
        WebClientResponseException exception = mock(WebClientResponseException.class);

        // When
        TravelDestination result = travelDestinationService.fallbackTravelDestination(destinationName, country, exception);

        // Then
        assertNotNull(result);
        assertEquals("No Details Available", result.getDestinationId());
        assertEquals(destinationName, result.getName());
        assertEquals(country, result.getCountry());
        assertEquals("No Details Available", result.getCity());
        assertEquals("Destination information temporarily unavailable due to service issues.", result.getDescription());
        assertEquals("No Details Available", result.getCategory());
        assertEquals("No Details Available", result.getBestSeasonToVisit());
        assertEquals("No Details Available", result.getAttractions());
    }

    @Test
    @DisplayName("Should successfully get attractions")
    void shouldSuccessfullyGetAttractions() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = "USA";

        // When
        String result = travelDestinationService.getAttractions(destinationName, country);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(destinationName));
        assertTrue(result.contains("alpine scenery"));
        assertTrue(result.contains("wildlife"));
        assertTrue(result.contains("Trail Ridge Road"));
        assertTrue(result.contains("Bear Lake"));
    }

    @Test
    @DisplayName("Should throw exception when attractions destination name is null")
    void shouldThrowExceptionWhenAttractionsDestinationNameIsNull() {
        // Given
        String destinationName = null;
        String country = "USA";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getAttractions(destinationName, country);
        });
        assertEquals("Destination name is required and cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when attractions country is null")
    void shouldThrowExceptionWhenAttractionsCountryIsNull() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getAttractions(destinationName, country);
        });
        assertEquals("Country is required and cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should return fallback message when rate limit exceeded")
    void shouldReturnFallbackMessageWhenRateLimitExceeded() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = "USA";
        io.github.resilience4j.ratelimiter.RequestNotPermitted requestNotPermitted = 
            mock(io.github.resilience4j.ratelimiter.RequestNotPermitted.class);

        // When
        String result = travelDestinationService.fallbackRateLimit(destinationName, country, requestNotPermitted);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("API rate limit exceeded"));
        assertTrue(result.contains(destinationName));
        assertTrue(result.contains("try again in one minute"));
    }

    @Test
    @DisplayName("Should handle empty destination name in attractions")
    void shouldHandleEmptyDestinationNameInAttractions() {
        // Given
        String destinationName = "";
        String country = "USA";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getAttractions(destinationName, country);
        });
        assertEquals("Destination name is required and cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle empty country in attractions")
    void shouldHandleEmptyCountryInAttractions() {
        // Given
        String destinationName = "Rocky Mountain";
        String country = "";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            travelDestinationService.getAttractions(destinationName, country);
        });
        assertEquals("Country is required and cannot be null or empty", exception.getMessage());
    }
}
