package com.p.circuitbreaker.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponse DTO Tests")
class ApiResponseTest {

    @Test
    @DisplayName("Should create successful ApiResponse with data")
    void shouldCreateSuccessfulApiResponseWithData() {
        // Given
        String testData = "test data";

        // When
        ApiResponse<String> response = ApiResponse.success(testData);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals(testData, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create successful ApiResponse with custom message")
    void shouldCreateSuccessfulApiResponseWithCustomMessage() {
        // Given
        String message = "Custom success message";
        String testData = "test data";

        // When
        ApiResponse<String> response = ApiResponse.success(message, testData);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(testData, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create error ApiResponse")
    void shouldCreateErrorApiResponse() {
        // Given
        String errorMessage = "Error occurred";

        // When
        ApiResponse<String> response = ApiResponse.error(errorMessage);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create error ApiResponse with errors map")
    void shouldCreateErrorApiResponseWithErrors() {
        // Given
        String errorMessage = "Validation failed";
        Map<String, String> errors = new HashMap<>();
        errors.put("field1", "Field 1 is required");
        errors.put("field2", "Field 2 is invalid");

        // When
        ApiResponse<Map<String, String>> response = ApiResponse.error(errorMessage, errors);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertEquals(errors, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create ApiResponse with constructor")
    void shouldCreateApiResponseWithConstructor() {
        // Given
        boolean success = true;
        String message = "Test message";
        String data = "test data";

        // When
        ApiResponse<String> response = new ApiResponse<>(success, message, data);

        // Then
        assertNotNull(response);
        assertEquals(success, response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create empty ApiResponse")
    void shouldCreateEmptyApiResponse() {
        // When
        ApiResponse<String> response = new ApiResponse<>();

        // Then
        assertNotNull(response);
        assertNotNull(response.getTimestamp());
        // Default values should be false for success, null for message and data
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetProperties() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();

        // When
        response.setSuccess(true);
        response.setMessage("Test message");
        response.setData("test data");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals("test data", response.getData());
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void shouldReturnCorrectToString() {
        // Given
        ApiResponse<String> response = ApiResponse.success("Test message", "test data");

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("success=true"));
        assertTrue(toString.contains("message='Test message'"));
        assertTrue(toString.contains("data=test data"));
        assertTrue(toString.contains("timestamp="));
    }

    @Test
    @DisplayName("Should handle null data in toString")
    void shouldHandleNullDataInToString() {
        // Given
        ApiResponse<String> response = ApiResponse.error("Error message");

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("success=false"));
        assertTrue(toString.contains("message='Error message'"));
        assertTrue(toString.contains("data=null"));
    }
}
