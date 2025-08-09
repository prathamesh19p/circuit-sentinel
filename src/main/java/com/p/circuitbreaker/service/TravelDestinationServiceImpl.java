package com.p.circuitbreaker.service;

import com.p.circuitbreaker.exception.DestinationNotFoundException;
import com.p.circuitbreaker.model.TravelDestination;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Service implementation for travel destination operations with circuit breaker and rate limiter patterns.
 */
@Service
public class TravelDestinationServiceImpl implements TravelDestinationService {

    private static final Logger log = LogManager.getLogger(TravelDestinationServiceImpl.class);
    private static final String NO_DETAILS_AVAILABLE = "No Details Available";
    private static final Duration TIMEOUT_DURATION = Duration.ofSeconds(10);

    private final WebClient webClient;
    private final RateLimiterRegistry rateLimiterRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public TravelDestinationServiceImpl(WebClient webClient, RateLimiterRegistry rateLimiterRegistry,
                                      CircuitBreakerRegistry circuitBreakerRegistry) {
        this.webClient = webClient;
        this.rateLimiterRegistry = rateLimiterRegistry;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    @CircuitBreaker(name = "travelDestination", fallbackMethod = "fallbackTravelDestination")
    public TravelDestination getDestinationDetails(String destinationName, String country) {
        log.info("Fetching destination details for: {} in country: {}", destinationName, country);
        
        validateInputParameters(destinationName, country);

        try {
            return webClient.get()
                    .uri("/travelDestination?destinationId={destinationId}&country={country}", destinationName, country)
                    .retrieve()
                    .bodyToMono(TravelDestination.class)
                    .timeout(TIMEOUT_DURATION)
                    .doOnSuccess(destination -> {
                        if (destination != null) {
                            log.info("Successfully retrieved destination: {}", destination.getName());
                        } else {
                            log.warn("Destination not found for: {} in country: {}", destinationName, country);
                        }
                    })
                    .doOnError(error -> log.error("Error retrieving destination: {}", error.getMessage()))
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            log.warn("Destination not found: {} in country: {}", destinationName, country);
            throw new DestinationNotFoundException("Destination not found: " + destinationName + " in " + country);
        } catch (Exception e) {
            log.error("Unexpected error while retrieving destination: {} in country: {}", destinationName, country, e);
            throw new RuntimeException("Failed to retrieve destination details", e);
        }
    }

    public TravelDestination fallbackTravelDestination(String destinationName, String country, Exception ex) {
        log.warn("Circuit breaker fallback triggered for destination: {} in country: {}. Error: {}", 
                destinationName, country, ex.getMessage());
        
        return TravelDestination.builder()
                .destinationId(NO_DETAILS_AVAILABLE)
                .name(destinationName)
                .country(country)
                .city(NO_DETAILS_AVAILABLE)
                .description("Destination information temporarily unavailable due to service issues.")
                .category(NO_DETAILS_AVAILABLE)
                .bestSeasonToVisit(NO_DETAILS_AVAILABLE)
                .attractions(NO_DETAILS_AVAILABLE)
                .build();
    }

    @Override
    @RateLimiter(name = "travelAttractions", fallbackMethod = "fallbackRateLimit")
    public String getAttractions(String destinationName, String country) {
        log.info("Fetching attractions for: {} in country: {}", destinationName, country);
        
        validateInputParameters(destinationName, country);

        // Simulate external service call
        return destinationName + " is renowned for its stunning alpine scenery, abundant wildlife, " +
               "and iconic trails such as Trail Ridge Road and Bear Lake. " +
               "The destination offers breathtaking views and unforgettable experiences for nature enthusiasts.";
    }

    public String fallbackRateLimit(String destinationName, String country, RequestNotPermitted requestNotPermitted) {
        log.warn("Rate limiter fallback triggered for destination: {} in country: {}", destinationName, country);
        return "API rate limit exceeded. Please try again in one minute to check the attractions at " + destinationName;
    }

    @PostConstruct
    public void postConstruct() {
        setupRateLimiterEventPublisher();
        setupCircuitBreakerEventPublisher();
    }

    private void validateInputParameters(String destinationName, String country) {
        if (!StringUtils.hasText(destinationName)) {
            throw new IllegalArgumentException("Destination name is required and cannot be null or empty");
        }
        if (!StringUtils.hasText(country)) {
            throw new IllegalArgumentException("Country is required and cannot be null or empty");
        }
    }

    private void setupRateLimiterEventPublisher() {
        var rateLimitEventPublisher = rateLimiterRegistry
                .rateLimiter("travelAttractions")
                .getEventPublisher();
        
        rateLimitEventPublisher.onEvent(event -> 
            log.info("Rate limiter event: {}", event.getEventType()));
        rateLimitEventPublisher.onSuccess(event -> 
            log.info("Rate limiter success: {}", event.getEventType()));
        rateLimitEventPublisher.onFailure(event -> 
            log.warn("Rate limiter failure: {}", event.getEventType()));
    }

    private void setupCircuitBreakerEventPublisher() {
        var circuitBreakerEventPublisher = circuitBreakerRegistry
                .circuitBreaker("travelDestination")
                .getEventPublisher();
        
        circuitBreakerEventPublisher.onEvent(event -> 
            log.info("Circuit breaker event: {}", event.getEventType()));
        circuitBreakerEventPublisher.onSuccess(event -> 
            log.info("Circuit breaker success: {}", event.getEventType()));
        circuitBreakerEventPublisher.onFailureRateExceeded(event -> 
            log.warn("Circuit breaker failure rate exceeded: {}", event.getEventType()));
        circuitBreakerEventPublisher.onSlowCallRateExceeded(event -> 
            log.warn("Circuit breaker slow call rate exceeded: {}", event.getEventType()));
    }
}
