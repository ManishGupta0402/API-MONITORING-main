package com.example.apimonitoring.controller;

import com.example.apimonitoring.dto.MonitoringStats;
import com.example.apimonitoring.model.HealthCheck;
import com.example.apimonitoring.service.HealthCheckService;
import com.example.apimonitoring.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    
    private final MonitoringService monitoringService;
    private final HealthCheckService healthCheckService;
    
    @Autowired
    public MonitoringController(MonitoringService monitoringService, HealthCheckService healthCheckService) {
        this.monitoringService = monitoringService;
        this.healthCheckService = healthCheckService;
    }
    
    @GetMapping("/stats")
    public ResponseEntity<List<MonitoringStats>> getAllMonitoringStats() {
        List<MonitoringStats> stats = monitoringService.getAllMonitoringStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/active")
    public ResponseEntity<List<MonitoringStats>> getActiveEndpointStats() {
        List<MonitoringStats> stats = monitoringService.getActiveEndpointStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/{endpointId}")
    public ResponseEntity<MonitoringStats> getMonitoringStats(@PathVariable Long endpointId) {
        try {
            MonitoringStats stats = monitoringService.getMonitoringStats(endpointId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/health-checks/{endpointId}")
    public ResponseEntity<List<HealthCheck>> getHealthCheckHistory(@PathVariable Long endpointId) {
        List<HealthCheck> healthChecks = healthCheckService.getHealthCheckHistory(endpointId);
        return ResponseEntity.ok(healthChecks);
    }
    
    @GetMapping("/health-checks/{endpointId}/since")
    public ResponseEntity<List<HealthCheck>> getHealthChecksSince(
            @PathVariable Long endpointId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        List<HealthCheck> healthChecks = healthCheckService.getHealthChecksSince(endpointId, since);
        return ResponseEntity.ok(healthChecks);
    }
    
    @PostMapping("/check/{endpointId}")
    public ResponseEntity<Void> performImmediateHealthCheck(@PathVariable Long endpointId) {
        try {
            monitoringService.performImmediateHealthCheck(endpointId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/check/all")
    public ResponseEntity<Void> performAllHealthChecks() {
        monitoringService.performScheduledHealthChecks();
        return ResponseEntity.ok().build();
    }
}