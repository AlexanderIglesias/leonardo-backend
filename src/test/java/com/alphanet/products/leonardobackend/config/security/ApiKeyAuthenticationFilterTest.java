package com.alphanet.products.leonardobackend.config.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ApiKeyAuthenticationFilter
 */
@ExtendWith(SpringExtension.class)
class ApiKeyAuthenticationFilterTest {

    @Test
    void testValidApiKeyConstructor() {
        String validApiKey = "valid-api-key-that-is-long-enough-12345";
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter(validApiKey);
        assertNotNull(filter);
    }

    @Test
    void testNullApiKeyConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ApiKeyAuthenticationFilter(null);
        });
    }

    @Test
    void testEmptyApiKeyConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ApiKeyAuthenticationFilter("");
        });
    }

    @Test
    void testBlankApiKeyConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ApiKeyAuthenticationFilter("   ");
        });
    }

    @Test
    void testRateLimitingInitialization() {
        // Use constructor with hardcoded values for testing
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter("test-api-key", 5, 60000, 300000);
        
        // Verify rate limiting map is initialized
        Object rateLimitMap = ReflectionTestUtils.getField(filter, "rateLimitMap");
        assertNotNull(rateLimitMap);
        
        // Verify rate limiting values are set correctly
        Integer maxFailedAttempts = (Integer) ReflectionTestUtils.getField(filter, "maxFailedAttemptsPerIp");
        Long rateLimitWindow = (Long) ReflectionTestUtils.getField(filter, "rateLimitWindowMs");
        Long logSuppressionWindow = (Long) ReflectionTestUtils.getField(filter, "logSuppressionWindowMs");
        
        assertEquals(5, maxFailedAttempts);
        assertEquals(60000L, rateLimitWindow);
        assertEquals(300000L, logSuppressionWindow);
    }

    @Test
    void testFilterStructure() {
        // Use constructor with hardcoded values for testing
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter("test-api-key", 5, 60000, 300000);
        
        // Verify the filter has the expected structure
        assertNotNull(filter);
        
        // Verify API key is set
        String apiKey = (String) ReflectionTestUtils.getField(filter, "apiKey");
        assertEquals("test-api-key", apiKey);
    }
}
