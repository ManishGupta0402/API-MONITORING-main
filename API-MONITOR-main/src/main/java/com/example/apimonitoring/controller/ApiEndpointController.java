package com.example.apimonitoring.controller;

import com.example.apimonitoring.dto.ApiEndpointRequest;
import com.example.apimonitoring.model.ApiEndpoint;
import com.example.apimonitoring.service.ApiEndpointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/endpoints")
@CrossOrigin(origins = "*")
public class ApiEndpointController {
    
    private final ApiEndpointService apiEndpointService;
    
    @Autowired
    public ApiEndpointController(ApiEndpointService apiEndpointService) {
        this.apiEndpointService = apiEndpointService;
    }
    
    @GetMapping
    public ResponseEntity<List<ApiEndpoint>> getAllEndpoints() {
        List<ApiEndpoint> endpoints = apiEndpointService.getAllEndpoints();
        return ResponseEntity.ok(endpoints);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ApiEndpoint>> getActiveEndpoints() {
        List<ApiEndpoint> endpoints = apiEndpointService.getActiveEndpoints();
        return ResponseEntity.ok(endpoints);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiEndpoint> getEndpointById(@PathVariable Long id) {
        Optional<ApiEndpoint> endpoint = apiEndpointService.getEndpointById(id);
        return endpoint.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ApiEndpoint> createEndpoint(@Valid @RequestBody ApiEndpointRequest request) {
        try {
            ApiEndpoint createdEndpoint = apiEndpointService.createEndpoint(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEndpoint);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiEndpoint> updateEndpoint(@PathVariable Long id, 
                                                     @Valid @RequestBody ApiEndpointRequest request) {
        try {
            ApiEndpoint updatedEndpoint = apiEndpointService.updateEndpoint(id, request);
            return ResponseEntity.ok(updatedEndpoint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndpoint(@PathVariable Long id) {
        try {
            apiEndpointService.deleteEndpoint(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/toggle")
    public ResponseEntity<ApiEndpoint> toggleEndpointStatus(@PathVariable Long id) {
        try {
            ApiEndpoint endpoint = apiEndpointService.toggleEndpointStatus(id);
            return ResponseEntity.ok(endpoint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveEndpointCount() {
        Long count = apiEndpointService.getActiveEndpointCount();
        return ResponseEntity.ok(count);
    }
}