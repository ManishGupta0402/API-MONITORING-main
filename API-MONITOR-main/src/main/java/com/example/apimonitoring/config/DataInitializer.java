package com.example.apimonitoring.config;

import com.example.apimonitoring.model.ApiEndpoint;
import com.example.apimonitoring.repository.ApiEndpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final ApiEndpointRepository apiEndpointRepository;
    
    @Autowired
    public DataInitializer(ApiEndpointRepository apiEndpointRepository) {
        this.apiEndpointRepository = apiEndpointRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no endpoints exist
        if (apiEndpointRepository.count() == 0) {
            logger.info("Initializing sample API endpoints...");
            
            // Sample endpoints for testing
            ApiEndpoint endpoint1 = new ApiEndpoint("JSONPlaceholder Posts", "https://jsonplaceholder.typicode.com/posts", "GET");
            endpoint1.setExpectedStatus(200);
            endpoint1.setTimeoutMs(5000);
            endpoint1.setCheckIntervalMs(30000L);
            endpoint1.setIsActive(true);
            
            ApiEndpoint endpoint2 = new ApiEndpoint("JSONPlaceholder Users", "https://jsonplaceholder.typicode.com/users", "GET");
            endpoint2.setExpectedStatus(200);
            endpoint2.setTimeoutMs(5000);
            endpoint2.setCheckIntervalMs(30000L);
            endpoint2.setIsActive(true);
            
            ApiEndpoint endpoint3 = new ApiEndpoint("HTTPBin Status", "https://httpbin.org/status/200", "GET");
            endpoint3.setExpectedStatus(200);
            endpoint3.setTimeoutMs(5000);
            endpoint3.setCheckIntervalMs(30000L);
            endpoint3.setIsActive(true);
            
            ApiEndpoint endpoint4 = new ApiEndpoint("Google Health Check", "https://www.google.com", "GET");
            endpoint4.setExpectedStatus(200);
            endpoint4.setTimeoutMs(5000);
            endpoint4.setCheckIntervalMs(60000L);
            endpoint4.setIsActive(true);
            
            apiEndpointRepository.save(endpoint1);
            apiEndpointRepository.save(endpoint2);
            apiEndpointRepository.save(endpoint3);
            apiEndpointRepository.save(endpoint4);
            
            logger.info("Sample API endpoints initialized successfully");
        } else {
            logger.info("API endpoints already exist, skipping initialization");
        }
    }
}