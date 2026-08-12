package com.sahildas.workflow.workflowservice.repository;

import com.sahildas.workflow.workflowservice.domain.WorkflowVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowVersionRepository extends JpaRepository<WorkflowVersion, UUID> {

    List<WorkflowVersion> findByWorkflowIdOrderByVersionNumberDesc(UUID workflowId);

    Optional<WorkflowVersion> findByWorkflowIdAndVersionNumber(UUID workflowId, Integer versionNumber);

}