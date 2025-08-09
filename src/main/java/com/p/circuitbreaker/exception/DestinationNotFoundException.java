package com.p.circuitbreaker.exception;

/**
 * Exception thrown when a travel destination is not found.
 */
public class DestinationNotFoundException extends RuntimeException {
    
    public DestinationNotFoundException(String message) {
        super(message);
    }
    
    public DestinationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
