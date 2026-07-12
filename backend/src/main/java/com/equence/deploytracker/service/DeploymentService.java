package com.equence.deploytracker.service;

import com.equence.deploytracker.dto.DeploymentWebhookRequest;
import com.equence.deploytracker.entity.Deployment;
import com.equence.deploytracker.entity.Service;
import com.equence.deploytracker.repository.DeploymentRepository;
import com.equence.deploytracker.repository.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service // Spring's @Service annotation — note the collision
public class DeploymentService {          // with your own entity class name "Service" above

    private final DeploymentRepository deploymentRepository;
    private final ServiceRepository serviceRepository;

    public DeploymentService(DeploymentRepository deploymentRepository,
                              ServiceRepository serviceRepository) {
        this.deploymentRepository = deploymentRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public Deployment recordDeployment(DeploymentWebhookRequest req) {
        // Auto-register the service if this is its first-ever deployment.
        // This is what makes the platform "generic" — Jenkins doesn't need
        // you to manually pre-create a service before it can report to it.
        Service service = serviceRepository.findByName(req.getServiceName())
            .orElseGet(() -> serviceRepository.save(
                new Service(req.getServiceName(), null, "Auto-registered from deployment webhook")
            ));

        Deployment.Status status = Deployment.Status.valueOf(req.getStatus().toUpperCase());

        Deployment deployment = new Deployment(
            service,
            req.getEnvironment(),
            req.getVersion(),
            status,
            req.getTriggeredBy(),
            req.getStartedAt(),
            req.getCompletedAt(),
            req.getFailureReason()
        );

        return deploymentRepository.save(deployment);
    }

    public List<Deployment> getHistory(String serviceName) {
        return deploymentRepository.findByService_NameOrderByCreatedAtDesc(serviceName);
    }

    public List<Deployment> getLatestPerService() {
        return deploymentRepository.findLatestPerService();
    }

    public List<Deployment> getAll() {
        return deploymentRepository.findAll();
    }
}
