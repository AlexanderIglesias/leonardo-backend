package com.alphanet.products.leonardobackend.exception;

public class MetricsException extends RuntimeException {

    private final String errorCode;

    public MetricsException(String message) {
        super(message);
        this.errorCode = "METRICS_ERROR";
    }

    public MetricsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MetricsException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "METRICS_ERROR";
    }

    public MetricsException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
