package com.example.apimonitoring.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);
    
    @Value("${monitoring.ssl.ignore-certificate-errors:false}")
    private boolean ignoreCertificateErrors;
    
    @Autowired
    private Environment environment;
    
    @PostConstruct
    public void logConfiguration() {
        System.out.println("🔧 WebClientConfig initialized!");
        System.out.println("🔧 SSL ignore setting: " + ignoreCertificateErrors);
        System.out.println("🔧 Active profiles: " + String.join(", ", environment.getActiveProfiles()));
        logger.error("🔧 FORCE LOG - WebClientConfig initialized with SSL ignore: {}", ignoreCertificateErrors);
    }
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        System.out.println("🚀 Building WebClient...");
        logger.error("🚀 FORCE LOG - Building WebClient with SSL ignore: {}", ignoreCertificateErrors);
        
        logger.info("=== CONFIGURING WEBCLIENT ===");
        logger.info("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));
        logger.info("Property monitoring.ssl.ignore-certificate-errors = {}", ignoreCertificateErrors);
        logger.info("Raw property value: {}", environment.getProperty("monitoring.ssl.ignore-certificate-errors"));
        
        WebClient.Builder builder = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .defaultHeader("User-Agent", "API-Monitoring-Service");
        
        if (ignoreCertificateErrors) {
            System.out.println("🚨 SSL VERIFICATION DISABLED!");
            logger.error("🚨 FORCE LOG - SSL certificate validation is DISABLED!");
            logger.warn("🚨 SSL certificate validation is DISABLED! This should only be used for local testing!");
            
            try {
                SslContext sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
                
                HttpClient httpClient = HttpClient.create()
                        .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
                
                builder.clientConnector(new ReactorClientHttpConnector(httpClient));
                System.out.println("✅ Insecure SSL context configured successfully!");
                logger.error("✅ FORCE LOG - Insecure SSL context successfully configured");
                
            } catch (SSLException e) {
                System.out.println("❌ Failed to create insecure SSL context: " + e.getMessage());
                logger.error("❌ Failed to create insecure SSL context", e);
                throw new RuntimeException("Failed to configure SSL context", e);
            }
        } else {
            System.out.println("🔒 SSL certificate validation is ENABLED");
            logger.error("🔒 FORCE LOG - SSL certificate validation is ENABLED (secure mode)");
        }
        
        System.out.println("🎯 WebClient configuration complete");
        logger.info("==============================");
        return builder;
    }
}