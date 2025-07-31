package com.example.apimonitoring.service;

import com.example.apimonitoring.dto.ApiEndpointRequest;
import com.example.apimonitoring.model.ApiEndpoint;
import com.example.apimonitoring.repository.ApiEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApiEndpointService {
    
    private final ApiEndpointRepository apiEndpointRepository;
    
    @Autowired
    public ApiEndpointService(ApiEndpointRepository apiEndpointRepository) {
        this.apiEndpointRepository = apiEndpointRepository;
    }
    
    public List<ApiEndpoint> getAllEndpoints() {
        return apiEndpointRepository.findAll();
    }
    
    public List<ApiEndpoint> getActiveEndpoints() {
        return apiEndpointRepository.findByIsActiveTrue();
    }
    
    public Optional<ApiEndpoint> getEndpointById(Long id) {
        return apiEndpointRepository.findById(id);
    }
    
    public ApiEndpoint createEndpoint(ApiEndpointRequest request) {
        ApiEndpoint endpoint = new ApiEndpoint();
        endpoint.setName(request.getName());
        endpoint.setUrl(request.getUrl());
        endpoint.setHttpMethod(request.getHttpMethod());
        endpoint.setExpectedStatus(request.getExpectedStatus());
        endpoint.setTimeoutMs(request.getTimeoutMs());
        endpoint.setCheckIntervalMs(request.getCheckIntervalMs());
        endpoint.setIsActive(request.getIsActive());
        
        return apiEndpointRepository.save(endpoint);
    }
    
    public ApiEndpoint updateEndpoint(Long id, ApiEndpointRequest request) {
        Optional<ApiEndpoint> optionalEndpoint = apiEndpointRepository.findById(id);
        if (optionalEndpoint.isPresent()) {
            ApiEndpoint endpoint = optionalEndpoint.get();
            endpoint.setName(request.getName());
            endpoint.setUrl(request.getUrl());
            endpoint.setHttpMethod(request.getHttpMethod());
            endpoint.setExpectedStatus(request.getExpectedStatus());
            endpoint.setTimeoutMs(request.getTimeoutMs());
            endpoint.setCheckIntervalMs(request.getCheckIntervalMs());
            endpoint.setIsActive(request.getIsActive());
            
            return apiEndpointRepository.save(endpoint);
        }
        throw new RuntimeException("Endpoint not found with id: " + id);
    }
    
    public void deleteEndpoint(Long id) {
        if (apiEndpointRepository.existsById(id)) {
            apiEndpointRepository.deleteById(id);
        } else {
            throw new RuntimeException("Endpoint not found with id: " + id);
        }
    }
    
    public ApiEndpoint toggleEndpointStatus(Long id) {
        Optional<ApiEndpoint> optionalEndpoint = apiEndpointRepository.findById(id);
        if (optionalEndpoint.isPresent()) {
            ApiEndpoint endpoint = optionalEndpoint.get();
            endpoint.setIsActive(!endpoint.getIsActive());
            return apiEndpointRepository.save(endpoint);
        }
        throw new RuntimeException("Endpoint not found with id: " + id);
    }
    
    public Long getActiveEndpointCount() {
        return apiEndpointRepository.countActiveEndpoints();
    }
}