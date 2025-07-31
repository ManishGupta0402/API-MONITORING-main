package com.example.apimonitoring.service;

import com.example.apimonitoring.model.ApiEndpoint;
import com.example.apimonitoring.model.HealthCheck;
import com.example.apimonitoring.repository.HealthCheckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HealthCheckService {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);
    
    private final HealthCheckRepository healthCheckRepository;
    private final WebClient webClient;
    
    @Autowired
    public HealthCheckService(HealthCheckRepository healthCheckRepository, WebClient.Builder webClientBuilder) {
        this.healthCheckRepository = healthCheckRepository;
        this.webClient = webClientBuilder.build();
    }
    
    public HealthCheck performHealthCheck(ApiEndpoint endpoint) {
        logger.info("Performing health check for endpoint: {}", endpoint.getName());
        
        HealthCheck healthCheck = new HealthCheck(endpoint);
        long startTime = System.currentTimeMillis();
        
        try {
            HttpMethod method = HttpMethod.valueOf(endpoint.getHttpMethod().toUpperCase());
            
            Integer statusCode = webClient
                .method(method)
                .uri(endpoint.getUrl())
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofMillis(endpoint.getTimeoutMs()))
                .map(response -> response.getStatusCode().value())
                .block();
            
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            healthCheck.setStatusCode(statusCode);
            healthCheck.setResponseTimeMs(responseTime);
            healthCheck.setIsSuccessful(statusCode.equals(endpoint.getExpectedStatus()));
            
            if (!healthCheck.getIsSuccessful()) {
                healthCheck.setErrorMessage("Expected status " + endpoint.getExpectedStatus() + 
                                          " but got " + statusCode);
            }
            
            logger.info("Health check completed for {}: Status={}, ResponseTime={}ms", 
                       endpoint.getName(), statusCode, responseTime);
            
        } catch (WebClientException e) {
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            healthCheck.setResponseTimeMs(responseTime);
            healthCheck.setIsSuccessful(false);
            healthCheck.setErrorMessage("Request failed: " + e.getMessage());
            
            logger.error("Health check failed for {}: {}", endpoint.getName(), e.getMessage());
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            
            healthCheck.setResponseTimeMs(responseTime);
            healthCheck.setIsSuccessful(false);
            healthCheck.setErrorMessage("Unexpected error: " + e.getMessage());
            
            logger.error("Unexpected error during health check for {}: {}", endpoint.getName(), e.getMessage());
        }
        
        return healthCheckRepository.save(healthCheck);
    }
    
    public List<HealthCheck> getHealthCheckHistory(Long endpointId) {
        return healthCheckRepository.findByApiEndpointIdOrderByCheckedAtDesc(endpointId);
    }
    
    public List<HealthCheck> getHealthChecksSince(Long endpointId, LocalDateTime since) {
        return healthCheckRepository.findHealthChecksSince(endpointId, since);
    }
    
    public Optional<HealthCheck> getLatestHealthCheck(Long endpointId) {
        return healthCheckRepository.findLatestHealthCheck(endpointId);
    }
    
    public void cleanupOldHealthChecks(LocalDateTime cutoffTime) {
        List<HealthCheck> oldChecks = healthCheckRepository.findOldHealthChecks(cutoffTime);
        if (!oldChecks.isEmpty()) {
            healthCheckRepository.deleteAll(oldChecks);
            logger.info("Cleaned up {} old health check records", oldChecks.size());
        }
    }
}