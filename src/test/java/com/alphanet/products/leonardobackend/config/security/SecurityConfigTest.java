package com.alphanet.products.leonardobackend.config.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SecurityConfig API key validation
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = {
    "api.security.enabled=true",
    "api.key=test-api-key-that-is-long-enough-to-pass-validation-12345"
})
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void testValidApiKeyConfiguration() {
        // This test should pass with a valid API key
        assertNotNull(securityConfig);
    }
}

/**
 * Unit tests for SecurityConfig API key validation
 */
class SecurityConfigValidationTest {

    @Test
    void testValidApiKeyValidation() {
        SecurityConfig securityConfig = new SecurityConfig();
        String validApiKey = "valid-api-key-that-is-long-enough-to-pass-validation-12345";
        
        // Use reflection to set the private field
        ReflectionTestUtils.setField(securityConfig, "apiKey", validApiKey);
        
        // This should not throw an exception
        assertDoesNotThrow(() -> {
            ReflectionTestUtils.invokeMethod(securityConfig, "validateApiKey");
        });
    }

    @Test
    void testNullApiKeyValidation() {
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Use reflection to set the private field to null
        ReflectionTestUtils.setField(securityConfig, "apiKey", null);
        
        // This should throw an exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ReflectionTestUtils.invokeMethod(securityConfig, "validateApiKey");
        });
        
        assertTrue(exception.getMessage().contains("API key cannot be null or empty"));
    }

    @Test
    void testEmptyApiKeyValidation() {
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Use reflection to set the private field to empty string
        ReflectionTestUtils.setField(securityConfig, "apiKey", "");
        
        // This should throw an exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ReflectionTestUtils.invokeMethod(securityConfig, "validateApiKey");
        });
        
        assertTrue(exception.getMessage().contains("API key cannot be null or empty"));
    }

    @Test
    void testShortApiKeyValidation() {
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Use reflection to set the private field to short API key
        ReflectionTestUtils.setField(securityConfig, "apiKey", "short");
        
        // This should throw an exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ReflectionTestUtils.invokeMethod(securityConfig, "validateApiKey");
        });
        
        assertTrue(exception.getMessage().contains("API key must be at least 32 characters long"));
    }

    @Test
    void testWeakApiKeyValidation() {
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Use reflection to set the private field to weak API key that passes length validation
        // "test" is too short, so use a longer weak pattern
        String weakApiKey = "testtesttesttesttesttesttesttesttesttesttesttest";
        ReflectionTestUtils.setField(securityConfig, "apiKey", weakApiKey);
        
        // This should throw an exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ReflectionTestUtils.invokeMethod(securityConfig, "validateApiKey");
        });
        
        assertTrue(exception.getMessage().contains("API key appears to be weak or insecure"));
    }

    @Test
    void testWeakPatternDetection() {
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Test various weak patterns that pass length validation
        String[] weakPatterns = {
            "testtesttesttesttesttesttesttesttesttesttesttest", // repeated "test"
            "demodemodemodemodemodemodemodemodemodemodemodemo", // repeated "demo"
            "defaultdefaultdefaultdefaultdefaultdefaultdefault", // repeated "default"
            "passwordpasswordpasswordpasswordpasswordpassword", // repeated "password"
            "1234567890123456789012345678901234567890123456789012345678901234", // only numbers
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnop", // only lowercase
            "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOP" // only uppercase
        };
        
        for (String weakPattern : weakPatterns) {
            ReflectionTestUtils.setField(securityConfig, "apiKey", weakPattern);
            
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                ReflectionTestUtils.invokeMethod(securityConfig, "validateApiKey");
            });
            
            assertTrue(exception.getMessage().contains("API key appears to be weak or insecure"));
        }
    }
}
