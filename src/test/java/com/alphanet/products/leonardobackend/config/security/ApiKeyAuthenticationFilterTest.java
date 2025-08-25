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
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter("test-api-key");
        
        // Verify rate limiting map is initialized
        Object rateLimitMap = ReflectionTestUtils.getField(filter, "rateLimitMap");
        assertNotNull(rateLimitMap);
        
        // Verify rate limiting constants are set
        Object maxFailedAttempts = ReflectionTestUtils.getField(filter, "MAX_FAILED_ATTEMPTS_PER_IP");
        Object rateLimitWindow = ReflectionTestUtils.getField(filter, "RATE_LIMIT_WINDOW_MS");
        Object logSuppressionWindow = ReflectionTestUtils.getField(filter, "LOG_SUPPRESSION_WINDOW_MS");
        
        assertNotNull(maxFailedAttempts);
        assertNotNull(rateLimitWindow);
        assertNotNull(logSuppressionWindow);
    }

    @Test
    void testFilterStructure() {
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter("test-api-key");
        
        // Verify the filter has the expected structure
        assertNotNull(filter);
        
        // Verify API key is set
        String apiKey = (String) ReflectionTestUtils.getField(filter, "apiKey");
        assertEquals("test-api-key", apiKey);
    }
}
