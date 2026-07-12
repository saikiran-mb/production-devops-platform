package com.equence.deploytracker.controller;

import com.equence.deploytracker.dto.DeploymentResponse;
import com.equence.deploytracker.dto.DeploymentWebhookRequest;
import com.equence.deploytracker.entity.Deployment;
import com.equence.deploytracker.service.DeploymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deployments")
public class DeploymentController {

    private final DeploymentService deploymentService;

    public DeploymentController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    // Jenkins calls this after every build.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeploymentResponse record(@Valid @RequestBody DeploymentWebhookRequest req) {
        Deployment saved = deploymentService.recordDeployment(req);
        return DeploymentResponse.from(saved);
    }

    @GetMapping
    public List<DeploymentResponse> all() {
        return deploymentService.getAll().stream()
            .map(DeploymentResponse::from)
            .collect(Collectors.toList());
    }

    @GetMapping("/latest")
    public List<DeploymentResponse> latest() {
        return deploymentService.getLatestPerService().stream()
            .map(DeploymentResponse::from)
            .collect(Collectors.toList());
    }

    @GetMapping("/service/{serviceName}")
    public List<DeploymentResponse> history(@PathVariable String serviceName) {
        return deploymentService.getHistory(serviceName).stream()
            .map(DeploymentResponse::from)
            .collect(Collectors.toList());
    }
}
