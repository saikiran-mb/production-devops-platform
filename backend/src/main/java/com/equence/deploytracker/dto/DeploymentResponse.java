package com.equence.deploytracker.dto;

import com.equence.deploytracker.entity.Deployment;
import java.time.LocalDateTime;

// What the API returns — decoupled from the JPA entity on purpose,
// so internal schema changes don't automatically change your public API contract.
public class DeploymentResponse {
    public Long id;
    public String serviceName;
    public String environment;
    public String version;
    public String status;
    public String triggeredBy;
    public LocalDateTime startedAt;
    public LocalDateTime completedAt;
    public Integer durationSeconds;
    public String failureReason;

    public static DeploymentResponse from(Deployment d) {
        DeploymentResponse r = new DeploymentResponse();
        r.id = d.getId();
        r.serviceName = d.getService().getName();
        r.environment = d.getEnvironment();
        r.version = d.getVersion();
        r.status = d.getStatus().name();
        r.triggeredBy = d.getTriggeredBy();
        r.startedAt = d.getStartedAt();
        r.completedAt = d.getCompletedAt();
        r.durationSeconds = d.getDurationSeconds();
        r.failureReason = d.getFailureReason();
        return r;
    }
}
