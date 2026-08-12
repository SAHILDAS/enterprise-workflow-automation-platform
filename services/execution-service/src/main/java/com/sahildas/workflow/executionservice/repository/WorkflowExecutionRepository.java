package com.sahildas.workflow.executionservice.repository;

import com.sahildas.workflow.executionservice.domain.WorkflowExecution;
import com.sahildas.workflow.executionservice.domain.enums.ExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowExecutionRepository extends JpaRepository<WorkflowExecution, UUID> {

    List<WorkflowExecution> findByWorkflowIdOrderByStartedAtDesc(UUID workflowId);

    List<WorkflowExecution> findByStatus(ExecutionStatus status);

}