package com.alphanet.products.leonardobackend.exception;

public class DataNotFoundException extends RuntimeException {

    private final String resourceType;
    private final String resourceId;

    public DataNotFoundException(String message) {
        super(message);
        this.resourceType = "UNKNOWN";
        this.resourceId = "UNKNOWN";
    }

    public DataNotFoundException(String message, String resourceType, String resourceId) {
        super(message);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public DataNotFoundException(String message, String resourceType, String resourceId, Throwable cause) {
        super(message, cause);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
