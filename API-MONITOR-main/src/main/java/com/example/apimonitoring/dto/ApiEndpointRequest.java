package com.example.apimonitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class ApiEndpointRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "URL is required")
    private String url;
    
    @NotBlank(message = "HTTP method is required")
    @Pattern(regexp = "GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS", message = "Invalid HTTP method")
    private String httpMethod;
    
    @Positive(message = "Expected status must be positive")
    private Integer expectedStatus = 200;
    
    @Positive(message = "Timeout must be positive")
    private Integer timeoutMs = 5000;
    
    @Positive(message = "Check interval must be positive")
    private Long checkIntervalMs = 30000L;
    
    private Boolean isActive = true;
    
    // Constructors
    public ApiEndpointRequest() {}
    
    public ApiEndpointRequest(String name, String url, String httpMethod) {
        this.name = name;
        this.url = url;
        this.httpMethod = httpMethod;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getHttpMethod() {
        return httpMethod;
    }
    
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    
    public Integer getExpectedStatus() {
        return expectedStatus;
    }
    
    public void setExpectedStatus(Integer expectedStatus) {
        this.expectedStatus = expectedStatus;
    }
    
    public Integer getTimeoutMs() {
        return timeoutMs;
    }
    
    public void setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
    
    public Long getCheckIntervalMs() {
        return checkIntervalMs;
    }
    
    public void setCheckIntervalMs(Long checkIntervalMs) {
        this.checkIntervalMs = checkIntervalMs;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}