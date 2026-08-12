package com.sahildas.workflow.workflowservice.repository;

import com.sahildas.workflow.workflowservice.domain.Workflow;
import com.sahildas.workflow.workflowservice.domain.enums.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {

    List<Workflow> findByStatus(WorkflowStatus status);

    boolean existsByNameIgnoreCase(String name);

}