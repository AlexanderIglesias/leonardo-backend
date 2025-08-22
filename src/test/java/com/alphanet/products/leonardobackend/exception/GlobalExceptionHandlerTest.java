package com.alphanet.products.leonardobackend.exception;

import com.alphanet.products.leonardobackend.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private ServletWebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/metrics/scalar");
        webRequest = new ServletWebRequest(request);
    }

    @Test
    @DisplayName("Should handle MetricsException correctly")
    void shouldHandleMetricsExceptionCorrectly() {
        // Given
        MetricsException ex = new MetricsException("Database connection failed", "DB_CONNECTION_ERROR");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMetricsException(ex, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("Error processing metrics request");
        assertThat(response.getBody().getDetails()).isEqualTo("Database connection failed");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/metrics/scalar");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle DataNotFoundException correctly")
    void shouldHandleDataNotFoundExceptionCorrectly() {
        // Given
        DataNotFoundException ex = new DataNotFoundException("Training center not found", "TrainingCenter", "123");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataNotFoundException(ex, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("Requested data not found");
        assertThat(response.getBody().getDetails()).isEqualTo("Training center not found");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/metrics/scalar");
    }

    @Test
    @DisplayName("Should handle DataAccessException correctly")
    void shouldHandleDataAccessExceptionCorrectly() {
        // Given
        DataAccessException ex = new DataAccessException("Connection timeout") {};

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataAccessException(ex, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("Database operation failed");
        assertThat(response.getBody().getDetails()).isEqualTo("Unable to access or modify data in the database");
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException correctly")
    void shouldHandleHttpMessageNotReadableExceptionCorrectly() {
        // Given
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid JSON format");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadableException(ex, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid request format");
        assertThat(response.getBody().getDetails()).isEqualTo("Request body is not readable or contains invalid JSON");
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void shouldHandleGenericExceptionCorrectly() {
        // Given
        Exception ex = new Exception("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(ex, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("Internal server error");
        assertThat(response.getBody().getDetails()).isEqualTo("An unexpected error occurred while processing your request");
    }

    @Test
    @DisplayName("Should include timestamp in all error responses")
    void shouldIncludeTimestampInAllErrorResponses() {
        // Given
        MetricsException ex = new MetricsException("Test error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMetricsException(ex, webRequest);

        // Then
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getTimestamp()).isBefore(java.time.LocalDateTime.now().plusSeconds(1));
    }

    @Test
    @DisplayName("Should include correct path in error responses")
    void shouldIncludeCorrectPathInErrorResponses() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/metrics/github-users");
        ServletWebRequest customWebRequest = new ServletWebRequest(request);
        
        MetricsException ex = new MetricsException("Test error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMetricsException(ex, customWebRequest);

        // Then
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/metrics/github-users");
    }
}
