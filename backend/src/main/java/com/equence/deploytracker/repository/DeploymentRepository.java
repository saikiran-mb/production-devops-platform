package com.equence.deploytracker.repository;

import com.equence.deploytracker.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    List<Deployment> findByService_NameOrderByCreatedAtDesc(String serviceName);

    List<Deployment> findByStatusOrderByCreatedAtDesc(Deployment.Status status);

    List<Deployment> findByEnvironmentOrderByCreatedAtDesc(String environment);

    // One row per service: its most recent deployment.
    // DISTINCT ON is Postgres-specific — worth knowing this is not portable SQL,
    // and being able to explain that tradeoff is a good interview point.
    @Query(value = """
        SELECT DISTINCT ON (service_id) *
        FROM deployments
        ORDER BY service_id, created_at DESC
        """, nativeQuery = true)
    List<Deployment> findLatestPerService();
}
