package com.sahildas.workflow.workflowservice.domain;

import com.sahildas.workflow.workflowservice.domain.enums.WorkflowStepType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workflow_steps")
public class WorkflowStep {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_version_id", nullable = false)
    private WorkflowVersion workflowVersion;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false, length = 50)
    private WorkflowStepType stepType;

    @Column(nullable = false)
    private Integer sequence;

    @Column(columnDefinition = "jsonb")
    private String configuration;

    @Column(name = "retry_policy", columnDefinition = "jsonb")
    private String retryPolicy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected WorkflowStep() {
    }

    public WorkflowStep(
            WorkflowVersion workflowVersion,
            String name,
            WorkflowStepType stepType,
            Integer sequence
    ) {
        this.id = UUID.randomUUID();
        this.workflowVersion = workflowVersion;
        this.name = name;
        this.stepType = stepType;
        this.sequence = sequence;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public WorkflowVersion getWorkflowVersion() {
        return workflowVersion;
    }

    public String getName() {
        return name;
    }

    public WorkflowStepType getStepType() {
        return stepType;
    }

    public Integer getSequence() {
        return sequence;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getRetryPolicy() {
        return retryPolicy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setRetryPolicy(String retryPolicy) {
        this.retryPolicy = retryPolicy;
    }
}