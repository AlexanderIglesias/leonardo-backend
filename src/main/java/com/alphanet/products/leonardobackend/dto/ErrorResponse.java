package com.alphanet.products.leonardobackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Structured error response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "500")
    private int status;

    @Schema(description = "Error message", example = "Internal server error occurred")
    private String message;

    @Schema(description = "Error details", example = "Database connection failed")
    private String details;

    @Schema(description = "Timestamp when error occurred")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Request path that caused the error", example = "/api/v1/metrics/scalar")
    private String path;

    @Schema(description = "List of validation errors (if any)")
    private List<ValidationError> validationErrors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Validation error details")
    public static class ValidationError {
        @Schema(description = "Field name with error", example = "email")
        private String field;

        @Schema(description = "Error message for the field", example = "Email is required")
        private String message;

        @Schema(description = "Rejected value", example = "invalid-email")
        private Object rejectedValue;
    }

    public ErrorResponse(int status, String message, String details, String path) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
