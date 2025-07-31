package com.example.apimonitoring.repository;

import com.example.apimonitoring.model.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
    
    List<HealthCheck> findByApiEndpointIdOrderByCheckedAtDesc(Long apiEndpointId);
    
    @Query("SELECT hc FROM HealthCheck hc WHERE hc.apiEndpoint.id = :endpointId AND hc.checkedAt >= :since ORDER BY hc.checkedAt DESC")
    List<HealthCheck> findHealthChecksSince(@Param("endpointId") Long endpointId, @Param("since") LocalDateTime since);
    
    @Query(value = "SELECT * FROM health_checks hc WHERE hc.api_endpoint_id = :endpointId ORDER BY hc.checked_at DESC LIMIT 1", nativeQuery = true)
    Optional<HealthCheck> findLatestHealthCheck(@Param("endpointId") Long endpointId);
    
    @Query("SELECT hc FROM HealthCheck hc WHERE hc.checkedAt < :cutoffTime")
    List<HealthCheck> findOldHealthChecks(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    @Query("SELECT AVG(hc.responseTimeMs) FROM HealthCheck hc WHERE hc.apiEndpoint.id = :endpointId AND hc.isSuccessful = true")
    Optional<Double> findAverageResponseTime(@Param("endpointId") Long endpointId);
    
    @Query("SELECT MIN(hc.responseTimeMs) FROM HealthCheck hc WHERE hc.apiEndpoint.id = :endpointId AND hc.isSuccessful = true")
    Optional<Long> findMinResponseTime(@Param("endpointId") Long endpointId);
    
    @Query("SELECT MAX(hc.responseTimeMs) FROM HealthCheck hc WHERE hc.apiEndpoint.id = :endpointId AND hc.isSuccessful = true")
    Optional<Long> findMaxResponseTime(@Param("endpointId") Long endpointId);
    
    @Query(value = "SELECT hc.response_time_ms FROM health_checks hc WHERE hc.api_endpoint_id = :endpointId AND hc.is_successful = true ORDER BY hc.checked_at DESC LIMIT 10", nativeQuery = true)
    List<Long> findRecentResponseTimes(@Param("endpointId") Long endpointId);
    
    @Query("SELECT COUNT(hc) FROM HealthCheck hc WHERE hc.apiEndpoint.id = :endpointId")
    Long countByApiEndpointId(@Param("endpointId") Long endpointId);
    
    @Query("SELECT COUNT(hc) FROM HealthCheck hc WHERE hc.apiEndpoint.id = :endpointId AND hc.isSuccessful = true")
    Long countSuccessfulByApiEndpointId(@Param("endpointId") Long endpointId);
}