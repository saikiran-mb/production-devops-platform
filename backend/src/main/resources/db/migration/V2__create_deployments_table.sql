CREATE TABLE deployments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    service_id BIGINT NOT NULL,

    environment VARCHAR(50) NOT NULL DEFAULT 'prod',

    version VARCHAR(100) NOT NULL,

    status VARCHAR(30) NOT NULL
        CHECK (status IN ('SUCCESS', 'FAILED', 'ROLLED_BACK', 'IN_PROGRESS')),

    triggered_by VARCHAR(100),

    started_at TIMESTAMP NOT NULL,

    completed_at TIMESTAMP NOT NULL,

    duration_seconds INTEGER,

    failure_reason TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_deployments_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_deployments_status
    ON deployments(status);

CREATE INDEX idx_deployments_service_id
    ON deployments(service_id);

CREATE INDEX idx_deployments_service_created_at
    ON deployments(service_id, created_at DESC);
