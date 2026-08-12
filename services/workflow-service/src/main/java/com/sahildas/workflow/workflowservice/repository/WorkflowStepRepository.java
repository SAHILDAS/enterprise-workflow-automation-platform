package com.sahildas.workflow.workflowservice.repository;

import com.sahildas.workflow.workflowservice.domain.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, UUID> {

    List<WorkflowStep> findByWorkflowVersionIdOrderBySequenceAsc(UUID workflowVersionId);

}