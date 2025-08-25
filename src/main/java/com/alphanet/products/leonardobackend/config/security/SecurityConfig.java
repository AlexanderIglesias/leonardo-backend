package com.alphanet.products.leonardobackend.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;

/**
 * Security Configuration for Leonardo Backend API
 *
 * This configuration sets up API key-based authentication for the REST API.
 * It allows public access to Swagger documentation and health endpoints
 * while requiring API key authentication for all other endpoints.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api.security.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {

    private static final int MIN_API_KEY_LENGTH = 32;
    private static final String API_KEY_VALIDATION_ERROR = "API key validation failed: ";

    @Value("${api.key}")
    private String apiKey;

    @PostConstruct
    public void validateApiKey() {
        if (!StringUtils.hasText(apiKey)) {
            String error = API_KEY_VALIDATION_ERROR + "API key cannot be null or empty";
            log.error(error);
            throw new IllegalStateException(error);
        }

        if (apiKey.length() < MIN_API_KEY_LENGTH) {
            String error = API_KEY_VALIDATION_ERROR + 
                String.format("API key must be at least %d characters long. Current length: %d", 
                    MIN_API_KEY_LENGTH, apiKey.length());
            log.error(error);
            throw new IllegalStateException(error);
        }

        // Check for common weak patterns
        if (isWeakApiKey(apiKey)) {
            String error = API_KEY_VALIDATION_ERROR + "API key appears to be weak or insecure";
            log.error(error);
            throw new IllegalStateException(error);
        }

        log.info("API key validation successful - Length: {} characters", apiKey.length());
    }

    private boolean isWeakApiKey(String key) {
        // Check for common weak patterns
        return key.equals("test") ||
               key.equals("demo") ||
               key.equals("default") ||
               key.equals("password") ||
               key.equals("1234567890") ||
               key.equals("abcdefghijklmnopqrstuvwxyz") ||
               key.equals("ABCDEFGHIJKLMNOPQRSTUVWXYZ") ||
               key.matches("^[a-zA-Z]+$") || // Only letters
               key.matches("^[0-9]+$") ||    // Only numbers
               key.matches("^[a-zA-Z0-9]+$") && key.length() < 64; // Alphanumeric but too short
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring API key-based security");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/info").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(apiKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public ApiKeyAuthenticationFilter apiKeyAuthenticationFilter() {
        return new ApiKeyAuthenticationFilter(apiKey);
    }
}

/**
 * Configuration for when security is disabled
 * This allows the application to run without any security for development or testing
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "api.security.enabled", havingValue = "false")
class SecurityDisabledConfig {

    @Bean
    public SecurityFilterChain disabledSecurityFilterChain(HttpSecurity http) throws Exception {
        log.warn("API Security is DISABLED - All endpoints are publicly accessible");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }
}
