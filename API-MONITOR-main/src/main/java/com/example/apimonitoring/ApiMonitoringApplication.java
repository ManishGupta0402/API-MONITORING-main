package com.example.apimonitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableScheduling
public class ApiMonitoringApplication {

    private static final Logger logger = LoggerFactory.getLogger(ApiMonitoringApplication.class);
    
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(ApiMonitoringApplication.class, args);
    }
    
    @PostConstruct
    public void logActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();
        
        logger.info("=== API MONITORING APPLICATION STARTED ===");
        logger.info("Active profiles: {}", activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "NONE");
        logger.info("Default profiles: {}", String.join(", ", defaultProfiles));
        logger.info("SSL ignore setting: {}", environment.getProperty("monitoring.ssl.ignore-certificate-errors", "NOT_SET"));
        logger.info("===========================================");
    }
}