package com.sahildas.workflow.executionservice.domain;

import com.sahildas.workflow.executionservice.domain.enums.ExecutionStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "workflow_executions")
public class WorkflowExecution {

    @Id
    private UUID id;

    @Column(name = "workflow_id", nullable = false)
    private UUID workflowId;

    @Column(name = "workflow_version_id")
    private UUID workflowVersionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExecutionStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "initiated_by", length = 100)
    private String initiatedBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_payload", columnDefinition = "jsonb")
    private String inputPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "output_payload", columnDefinition = "jsonb")
    private String outputPayload;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Version
    private Long version;

    @OneToMany(
            mappedBy = "workflowExecution",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<StepExecution> stepExecutions = new ArrayList<>();

    protected WorkflowExecution() {
    }

    public WorkflowExecution(UUID workflowId, UUID workflowVersionId, String initiatedBy) {
        this.id = UUID.randomUUID();
        this.workflowId = workflowId;
        this.workflowVersionId = workflowVersionId;
        this.initiatedBy = initiatedBy;
        this.status = ExecutionStatus.PENDING;
        this.startedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkflowId() {
        return workflowId;
    }

    public UUID getWorkflowVersionId() {
        return workflowVersionId;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public String getInputPayload() {
        return inputPayload;
    }

    public String getOutputPayload() {
        return outputPayload;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getVersion() {
        return version;
    }

    public List<StepExecution> getStepExecutions() {
        return stepExecutions;
    }

    public void start() {
        this.status = ExecutionStatus.RUNNING;
    }

    public void complete(String outputPayload) {
        this.status = ExecutionStatus.COMPLETED;
        this.outputPayload = outputPayload;
        this.completedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        this.status = ExecutionStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
    }

    public void cancel() {
        this.status = ExecutionStatus.CANCELLED;
        this.completedAt = Instant.now();
    }

    public void setInputPayload(String inputPayload) {
        this.inputPayload = inputPayload;
    }
}