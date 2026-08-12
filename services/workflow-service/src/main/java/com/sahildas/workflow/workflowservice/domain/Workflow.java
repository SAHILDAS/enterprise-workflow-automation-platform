package com.sahildas.workflow.workflowservice.domain;

import com.sahildas.workflow.workflowservice.domain.enums.WorkflowStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflows")
public class Workflow {

    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkflowStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    @OneToMany(
            mappedBy = "workflow",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<WorkflowVersion> versions = new ArrayList<>();

    protected Workflow() {
    }

    public Workflow(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.status = WorkflowStatus.DRAFT;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public List<WorkflowVersion> getVersions() {
        return versions;
    }

    public void activate() {
        this.status = WorkflowStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = WorkflowStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void archive() {
        this.status = WorkflowStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }
}