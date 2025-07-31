package com.example.apimonitoring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_checks")
public class HealthCheck {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_endpoint_id", nullable = false)
    @JsonBackReference
    private ApiEndpoint apiEndpoint;
    
    @Column(name = "status_code")
    private Integer statusCode;
    
    @Column(name = "response_time_ms")
    private Long responseTimeMs;
    
    @Column(name = "is_successful")
    private Boolean isSuccessful;
    
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
    
    @Column(name = "checked_at")
    private LocalDateTime checkedAt;
    
    @PrePersist
    protected void onCreate() {
        checkedAt = LocalDateTime.now();
    }
    
    // Constructors
    public HealthCheck() {}
    
    public HealthCheck(ApiEndpoint apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ApiEndpoint getApiEndpoint() {
        return apiEndpoint;
    }
    
    public void setApiEndpoint(ApiEndpoint apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    
    public Long getResponseTimeMs() {
        return responseTimeMs;
    }
    
    public void setResponseTimeMs(Long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }
    
    public Boolean getIsSuccessful() {
        return isSuccessful;
    }
    
    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
    
    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}