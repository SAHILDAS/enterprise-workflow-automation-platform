package com.sahildas.workflow.executionservice.domain;

import com.sahildas.workflow.executionservice.domain.enums.StepExecutionStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "step_executions")
public class StepExecution {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_execution_id", nullable = false)
    private WorkflowExecution workflowExecution;

    @Column(name = "step_name", nullable = false, length = 200)
    private String stepName;

    @Column(name = "step_type", nullable = false, length = 50)
    private String stepType;

    @Column(nullable = false)
    private Integer sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StepExecutionStatus status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_payload", columnDefinition = "jsonb")
    private String inputPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "output_payload", columnDefinition = "jsonb")
    private String outputPayload;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    protected StepExecution() {
    }

    public StepExecution(
            WorkflowExecution workflowExecution,
            String stepName,
            String stepType,
            Integer sequence
    ) {
        this.id = UUID.randomUUID();
        this.workflowExecution = workflowExecution;
        this.stepName = stepName;
        this.stepType = stepType;
        this.sequence = sequence;
        this.status = StepExecutionStatus.PENDING;
        this.retryCount = 0;
    }

    public UUID getId() {
        return id;
    }

    public WorkflowExecution getWorkflowExecution() {
        return workflowExecution;
    }

    public String getStepName() {
        return stepName;
    }

    public String getStepType() {
        return stepType;
    }

    public Integer getSequence() {
        return sequence;
    }

    public StepExecutionStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
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

    public Integer getRetryCount() {
        return retryCount;
    }

    public void start() {
        this.status = StepExecutionStatus.RUNNING;
        this.startedAt = Instant.now();
    }

    public void complete(String outputPayload) {
        this.status = StepExecutionStatus.COMPLETED;
        this.outputPayload = outputPayload;
        this.completedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        this.status = StepExecutionStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
    }

    public void incrementRetry() {
        this.retryCount++;
    }

    public void setInputPayload(String inputPayload) {
        this.inputPayload = inputPayload;
    }
}