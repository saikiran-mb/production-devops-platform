package com.equence.deploytracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "repo_url", length = 500)
    private String repoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Service() {}

    public Service(String name, String repoUrl, String description) {
        this.name = name;
        this.repoUrl = repoUrl;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRepoUrl() { return repoUrl; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
