package com.sahildas.workflow.workflowservice.domain;

import com.sahildas.workflow.workflowservice.domain.enums.WorkflowVersionStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "workflow_versions",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_workflow_version",
                columnNames = {"workflow_id", "version_number"}
        )
)
public class WorkflowVersion {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkflowVersionStatus status;

    @Column(columnDefinition = "jsonb")
    private String definition;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @OneToMany(
            mappedBy = "workflowVersion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<WorkflowStep> steps = new ArrayList<>();

    protected WorkflowVersion() {
    }

    public WorkflowVersion(Workflow workflow, Integer versionNumber, String createdBy) {
        this.id = UUID.randomUUID();
        this.workflow = workflow;
        this.versionNumber = versionNumber;
        this.status = WorkflowVersionStatus.DRAFT;
        this.createdAt = Instant.now();
        this.createdBy = createdBy;
    }

    public UUID getId() {
        return id;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public WorkflowVersionStatus getStatus() {
        return status;
    }

    public String getDefinition() {
        return definition;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public List<WorkflowStep> getSteps() {
        return steps;
    }

    public void activate() {
        this.status = WorkflowVersionStatus.ACTIVE;
    }

    public void deprecate() {
        this.status = WorkflowVersionStatus.DEPRECATED;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}