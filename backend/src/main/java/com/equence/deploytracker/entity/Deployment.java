package com.equence.deploytracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deployments")
public class Deployment {

    public enum Status { SUCCESS, FAILED, ROLLED_BACK, IN_PROGRESS }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(nullable = false, length = 50)
    private String environment = "prod";

    @Column(nullable = false, length = 100)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @Column(name = "triggered_by", length = 100)
    private String triggeredBy;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Deployment() {}

    public Deployment(Service service, String environment, String version, Status status,
                       String triggeredBy, LocalDateTime startedAt, LocalDateTime completedAt,
                       String failureReason) {
        this.service = service;
        this.environment = environment != null ? environment : "prod";
        this.version = version;
        this.status = status;
        this.triggeredBy = triggeredBy;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.failureReason = failureReason;
        this.durationSeconds = (int) java.time.Duration.between(startedAt, completedAt).getSeconds();
    }

    public Long getId() { return id; }
    public Service getService() { return service; }
    public String getEnvironment() { return environment; }
    public String getVersion() { return version; }
    public Status getStatus() { return status; }
    public String getTriggeredBy() { return triggeredBy; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public String getFailureReason() { return failureReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
