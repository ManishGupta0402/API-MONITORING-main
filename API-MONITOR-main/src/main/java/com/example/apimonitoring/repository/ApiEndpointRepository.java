package com.example.apimonitoring.repository;

import com.example.apimonitoring.model.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, Long> {
    
    List<ApiEndpoint> findByIsActiveTrue();
    
    List<ApiEndpoint> findByIsActiveFalse();
    
    @Query("SELECT ae FROM ApiEndpoint ae WHERE ae.isActive = true ORDER BY ae.name")
    List<ApiEndpoint> findActiveEndpointsOrderByName();
    
    @Query("SELECT COUNT(ae) FROM ApiEndpoint ae WHERE ae.isActive = true")
    Long countActiveEndpoints();
}