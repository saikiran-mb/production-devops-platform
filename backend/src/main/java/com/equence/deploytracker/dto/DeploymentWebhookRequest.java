package com.equence.deploytracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

// This is the payload shape Jenkins POSTs after a build finishes.
public class DeploymentWebhookRequest {

    @NotBlank
    private String serviceName;

    private String environment; // optional — defaults to "prod" if omitted

    @NotBlank
    private String version;

    @NotBlank
    private String status; // SUCCESS, FAILED, ROLLED_BACK

    private String triggeredBy;

    @NotNull
    private LocalDateTime startedAt;

    @NotNull
    private LocalDateTime completedAt;

    private String failureReason;

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(String triggeredBy) { this.triggeredBy = triggeredBy; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
