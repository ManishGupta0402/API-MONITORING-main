package com.example.apimonitoring.service;

import com.example.apimonitoring.dto.MonitoringStats;
import com.example.apimonitoring.model.ApiEndpoint;
import com.example.apimonitoring.model.HealthCheck;
import com.example.apimonitoring.repository.HealthCheckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    
    private final ApiEndpointService apiEndpointService;
    private final HealthCheckService healthCheckService;
    private final HealthCheckRepository healthCheckRepository;
    
    @Autowired
    public MonitoringService(ApiEndpointService apiEndpointService, 
                           HealthCheckService healthCheckService,
                           HealthCheckRepository healthCheckRepository) {
        this.apiEndpointService = apiEndpointService;
        this.healthCheckService = healthCheckService;
        this.healthCheckRepository = healthCheckRepository;
    }
    
    @Scheduled(fixedRateString = "${monitoring.check-interval:30000}")
    public void performScheduledHealthChecks() {
        logger.info("Starting scheduled health checks");
        
        List<ApiEndpoint> activeEndpoints = apiEndpointService.getActiveEndpoints();
        
        for (ApiEndpoint endpoint : activeEndpoints) {
            try {
                healthCheckService.performHealthCheck(endpoint);
            } catch (Exception e) {
                logger.error("Error performing health check for endpoint {}: {}", 
                           endpoint.getName(), e.getMessage());
            }
        }
        
        logger.info("Completed scheduled health checks for {} endpoints", activeEndpoints.size());
    }
    
    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
    public void cleanupOldHealthChecks() {
        logger.info("Starting cleanup of old health check records");
        
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(7); // Keep 7 days of history
        healthCheckService.cleanupOldHealthChecks(cutoffTime);
        
        logger.info("Completed cleanup of old health check records");
    }
    
    public List<MonitoringStats> getAllMonitoringStats() {
        List<ApiEndpoint> endpoints = apiEndpointService.getAllEndpoints();
        List<MonitoringStats> statsList = new ArrayList<>();
        
        for (ApiEndpoint endpoint : endpoints) {
            MonitoringStats stats = getMonitoringStats(endpoint.getId());
            statsList.add(stats);
        }
        
        return statsList;
    }
    
    public MonitoringStats getMonitoringStats(Long endpointId) {
        Optional<ApiEndpoint> optionalEndpoint = apiEndpointService.getEndpointById(endpointId);
        
        if (optionalEndpoint.isEmpty()) {
            throw new RuntimeException("Endpoint not found with id: " + endpointId);
        }
        
        ApiEndpoint endpoint = optionalEndpoint.get();
        MonitoringStats stats = new MonitoringStats(endpoint.getId(), endpoint.getName(), endpoint.getUrl());
        
        // Get latest health check
        Optional<HealthCheck> latestCheck = healthCheckService.getLatestHealthCheck(endpointId);
        if (latestCheck.isPresent()) {
            HealthCheck latest = latestCheck.get();
            stats.setIsHealthy(latest.getIsSuccessful());
            stats.setLastCheckTime(latest.getCheckedAt());
            stats.setLastError(latest.getErrorMessage());
            stats.setLatestResponseTime(latest.getResponseTimeMs());
        } else {
            stats.setIsHealthy(null);
        }
        
        // Calculate average response time
        Optional<Double> avgResponseTime = healthCheckRepository.findAverageResponseTime(endpointId);
        if (avgResponseTime.isPresent()) {
            stats.setAverageResponseTime(avgResponseTime.get().longValue());
        }
        
        // Get min and max response times
        Optional<Long> minResponseTime = healthCheckRepository.findMinResponseTime(endpointId);
        if (minResponseTime.isPresent()) {
            stats.setMinResponseTime(minResponseTime.get());
        }
        
        Optional<Long> maxResponseTime = healthCheckRepository.findMaxResponseTime(endpointId);
        if (maxResponseTime.isPresent()) {
            stats.setMaxResponseTime(maxResponseTime.get());
        }
        
        // Get recent response times for trend analysis
        List<Long> recentResponseTimes = healthCheckRepository.findRecentResponseTimes(endpointId);
        stats.setRecentResponseTimes(recentResponseTimes);
        
        // Calculate uptime percentage
        Long totalChecks = healthCheckRepository.countByApiEndpointId(endpointId);
        Long successfulChecks = healthCheckRepository.countSuccessfulByApiEndpointId(endpointId);
        
        stats.setTotalChecks(totalChecks.intValue());
        stats.setSuccessfulChecks(successfulChecks.intValue());
        
        if (totalChecks > 0) {
            double uptime = (successfulChecks.doubleValue() / totalChecks.doubleValue()) * 100.0;
            stats.setUptime(uptime);
        } else {
            stats.setUptime(null);
        }
        
        return stats;
    }
    
    public List<MonitoringStats> getActiveEndpointStats() {
        List<ApiEndpoint> activeEndpoints = apiEndpointService.getActiveEndpoints();
        List<MonitoringStats> statsList = new ArrayList<>();
        
        for (ApiEndpoint endpoint : activeEndpoints) {
            MonitoringStats stats = getMonitoringStats(endpoint.getId());
            statsList.add(stats);
        }
        
        return statsList;
    }
    
    public void performImmediateHealthCheck(Long endpointId) {
        Optional<ApiEndpoint> optionalEndpoint = apiEndpointService.getEndpointById(endpointId);
        
        if (optionalEndpoint.isEmpty()) {
            throw new RuntimeException("Endpoint not found with id: " + endpointId);
        }
        
        ApiEndpoint endpoint = optionalEndpoint.get();
        healthCheckService.performHealthCheck(endpoint);
    }
}