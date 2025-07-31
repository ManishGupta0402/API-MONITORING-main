package com.example.apimonitoring.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MonitoringStats {
    
    private Long endpointId;
    private String endpointName;
    private String url;
    private Boolean isHealthy;
    private Long averageResponseTime;
    private Long latestResponseTime;
    private Long minResponseTime;
    private Long maxResponseTime;
    private List<Long> recentResponseTimes; // Last 10 response times for trend analysis
    private Double uptime;
    private Integer totalChecks;
    private Integer successfulChecks;
    private LocalDateTime lastCheckTime;
    private String lastError;
    
    // Constructors
    public MonitoringStats() {}
    
    public MonitoringStats(Long endpointId, String endpointName, String url) {
        this.endpointId = endpointId;
        this.endpointName = endpointName;
        this.url = url;
    }
    
    // Getters and Setters
    public Long getEndpointId() {
        return endpointId;
    }
    
    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }
    
    public String getEndpointName() {
        return endpointName;
    }
    
    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Boolean getIsHealthy() {
        return isHealthy;
    }
    
    public void setIsHealthy(Boolean isHealthy) {
        this.isHealthy = isHealthy;
    }
    
    public Long getAverageResponseTime() {
        return averageResponseTime;
    }
    
    public void setAverageResponseTime(Long averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }
    
    public Long getLatestResponseTime() {
        return latestResponseTime;
    }
    
    public void setLatestResponseTime(Long latestResponseTime) {
        this.latestResponseTime = latestResponseTime;
    }
    
    public Long getMinResponseTime() {
        return minResponseTime;
    }
    
    public void setMinResponseTime(Long minResponseTime) {
        this.minResponseTime = minResponseTime;
    }
    
    public Long getMaxResponseTime() {
        return maxResponseTime;
    }
    
    public void setMaxResponseTime(Long maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }
    
    public List<Long> getRecentResponseTimes() {
        return recentResponseTimes;
    }
    
    public void setRecentResponseTimes(List<Long> recentResponseTimes) {
        this.recentResponseTimes = recentResponseTimes;
    }
    
    public Double getUptime() {
        return uptime;
    }
    
    public void setUptime(Double uptime) {
        this.uptime = uptime;
    }
    
    public Integer getTotalChecks() {
        return totalChecks;
    }
    
    public void setTotalChecks(Integer totalChecks) {
        this.totalChecks = totalChecks;
    }
    
    public Integer getSuccessfulChecks() {
        return successfulChecks;
    }
    
    public void setSuccessfulChecks(Integer successfulChecks) {
        this.successfulChecks = successfulChecks;
    }
    
    public LocalDateTime getLastCheckTime() {
        return lastCheckTime;
    }
    
    public void setLastCheckTime(LocalDateTime lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
    
    public String getLastError() {
        return lastError;
    }
    
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
    
    // Helper methods for dashboard display
    public String getFormattedLatestResponseTime() {
        return latestResponseTime != null ? latestResponseTime + "ms" : "N/A";
    }
    
    public String getFormattedAverageResponseTime() {
        return averageResponseTime != null ? averageResponseTime + "ms" : "N/A";
    }
    
    public String getFormattedMinResponseTime() {
        return minResponseTime != null ? minResponseTime + "ms" : "N/A";
    }
    
    public String getFormattedMaxResponseTime() {
        return maxResponseTime != null ? maxResponseTime + "ms" : "N/A";
    }
    
    public String getResponseTimeRange() {
        if (minResponseTime != null && maxResponseTime != null) {
            return minResponseTime + "-" + maxResponseTime + "ms";
        }
        return "N/A";
    }
}