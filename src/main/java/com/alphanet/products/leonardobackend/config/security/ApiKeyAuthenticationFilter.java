package com.alphanet.products.leonardobackend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.lang.NonNull;

/**
 * API Key Authentication Filter
 *
 * This filter validates the X-API-Key header for incoming requests.
 * If the API key is valid, it sets the authentication in the security context.
 * Implements configurable rate limiting for failed authentication attempts to prevent log flooding attacks.
 */
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String AUTHENTICATED_USER = "leonardo-gpt-agent";
    
    // Configurable rate limiting parameters - must be defined in application.properties
    @Value("${api.security.rate-limit.max-attempts}")
    private int maxFailedAttemptsPerIp;
    
    @Value("${api.security.rate-limit.window-ms}")
    private long rateLimitWindowMs;
    
    @Value("${api.security.rate-limit.log-suppression-ms}")
    private long logSuppressionWindowMs;
    
    private final String apiKey;
    
    // Rate limiting storage: IP -> (attempts, first attempt time, last log time)
    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    public ApiKeyAuthenticationFilter(String apiKey) {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalArgumentException("API key cannot be null or empty");
        }
        this.apiKey = apiKey;
        log.debug("ApiKeyAuthenticationFilter initialized with API key of length: {}", apiKey.length());
    }

    /**
     * Constructor for testing purposes with hardcoded rate limiting values
     */
    public ApiKeyAuthenticationFilter(String apiKey, int maxFailedAttemptsPerIp, long rateLimitWindowMs, long logSuppressionWindowMs) {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalArgumentException("API key cannot be null or empty");
        }
        this.apiKey = apiKey;
        this.maxFailedAttemptsPerIp = maxFailedAttemptsPerIp;
        this.rateLimitWindowMs = rateLimitWindowMs;
        this.logSuppressionWindowMs = logSuppressionWindowMs;
        log.debug("ApiKeyAuthenticationFilter initialized for testing with API key of length: {} and custom rate limiting", apiKey.length());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader(API_KEY_HEADER);
        String clientIp = getClientIpAddress(request);

        if (StringUtils.hasText(requestApiKey) && apiKey.equals(requestApiKey)) {
            // Valid API key - set authentication
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(AUTHENTICATED_USER, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Reset rate limiting for successful authentication
            rateLimitMap.remove(clientIp);

            log.debug("API key authentication successful for request: {} from IP: {}",
                     getSanitizedRequestInfo(request), clientIp);
        } else {
            // Handle failed authentication with rate limiting
            handleFailedAuthentication(request, clientIp);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Handles failed authentication with rate limiting to prevent log flooding
     */
    private void handleFailedAuthentication(HttpServletRequest request, String clientIp) {
        long currentTime = System.currentTimeMillis();
        RateLimitInfo rateLimitInfo = rateLimitMap.computeIfAbsent(clientIp, 
            k -> new RateLimitInfo(currentTime));

        // Check if we should log this failure
        boolean shouldLog = shouldLogFailedAttempt(clientIp, rateLimitInfo, currentTime);
        
        if (shouldLog) {
            log.warn("Authentication failed for IP: {} - Request: {} - Failed attempts: {}",
                    clientIp, getSanitizedRequestInfo(request), rateLimitInfo.attempts.get());
            
            // Update last log time
            rateLimitInfo.lastLogTime = currentTime;
        }
        
        // Increment attempt counter
        rateLimitInfo.attempts.incrementAndGet();
    }

    /**
     * Determines if a failed authentication attempt should be logged
     */
    private boolean shouldLogFailedAttempt(String clientIp, RateLimitInfo rateLimitInfo, long currentTime) {
        // Always log first few attempts
        if (rateLimitInfo.attempts.get() <= 3) {
            return true;
        }
        
        // Check if we're within the rate limit window
        if (currentTime - rateLimitInfo.firstAttemptTime < rateLimitWindowMs) {
            // Within rate limit window - only log if we haven't logged recently
            return currentTime - rateLimitInfo.lastLogTime > logSuppressionWindowMs;
        } else {
            // Outside rate limit window - reset and log
            rateLimitInfo.reset(currentTime);
            return true;
        }
    }

    /**
     * Returns sanitized request information to prevent sensitive data exposure
     */
    private String getSanitizedRequestInfo(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        // Sanitize URI to remove potential sensitive information
        String sanitizedUri = sanitizeUri(uri);
        
        return method + " " + sanitizedUri;
    }

    /**
     * Sanitizes URI to remove query strings only
     * Since current API endpoints don't have sensitive path parameters (IDs, UUIDs),
     * we only need to remove query strings to prevent sensitive query parameters
     */
    private String sanitizeUri(String uri) {
        if (uri == null) {
            return "null";
        }
        
        // Remove query string to prevent sensitive parameters
        // This is sufficient for the current API structure
        return uri.split("\\?")[0];
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * Inner class to track rate limiting information per IP
     */
    private static class RateLimitInfo {
        private final AtomicInteger attempts;
        private long firstAttemptTime;
        private long lastLogTime;

        public RateLimitInfo(long currentTime) {
            this.attempts = new AtomicInteger(0);
            this.firstAttemptTime = currentTime;
            this.lastLogTime = currentTime;
        }

        public void reset(long currentTime) {
            this.attempts.set(0);
            this.firstAttemptTime = currentTime;
            this.lastLogTime = currentTime;
        }
    }
}
