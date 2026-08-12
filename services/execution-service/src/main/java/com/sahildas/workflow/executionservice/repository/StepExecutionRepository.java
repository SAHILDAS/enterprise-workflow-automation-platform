package com.sahildas.workflow.executionservice.repository;

import com.sahildas.workflow.executionservice.domain.StepExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StepExecutionRepository extends JpaRepository<StepExecution, UUID> {

    List<StepExecution> findByWorkflowExecutionIdOrderBySequenceAsc(UUID workflowExecutionId);

}