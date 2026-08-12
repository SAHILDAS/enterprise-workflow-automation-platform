package com.sahildas.workflow.workflowservice.service;

import com.sahildas.workflow.workflowservice.domain.Workflow;
import com.sahildas.workflow.workflowservice.dto.request.CreateWorkflowRequest;
import com.sahildas.workflow.workflowservice.dto.request.UpdateWorkflowRequest;
import com.sahildas.workflow.workflowservice.dto.response.WorkflowResponse;
import com.sahildas.workflow.workflowservice.exception.WorkflowAlreadyExistsException;
import com.sahildas.workflow.workflowservice.exception.WorkflowNotFoundException;
import com.sahildas.workflow.workflowservice.repository.WorkflowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public WorkflowResponse createWorkflow(CreateWorkflowRequest request) {

        if (workflowRepository.existsByNameIgnoreCase(request.name())) {
            throw new WorkflowAlreadyExistsException(request.name());
        }

        Workflow workflow = new Workflow(request.name(), request.description());

        Workflow saved = workflowRepository.save(workflow);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponse> getAllWorkflows() {
        return workflowRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkflowResponse getWorkflow(UUID workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowNotFoundException(workflowId));

        return toResponse(workflow);
    }

    public WorkflowResponse updateWorkflow(UUID workflowId, UpdateWorkflowRequest request) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowNotFoundException(workflowId));

        if (!workflow.getName().equalsIgnoreCase(request.name())
                && workflowRepository.existsByNameIgnoreCase(request.name())) {
            throw new WorkflowAlreadyExistsException(request.name());
        }

        Workflow updated = new Workflow(request.name(), request.description());
        // Preserve identity and lifecycle fields manually for now
        // We will refactor this in a later phase when we introduce richer domain behavior.

        return toResponse(workflowRepository.save(workflow));
    }

    public WorkflowResponse activateWorkflow(UUID workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowNotFoundException(workflowId));

        workflow.activate();

        return toResponse(workflowRepository.save(workflow));
    }

    public WorkflowResponse deactivateWorkflow(UUID workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowNotFoundException(workflowId));

        workflow.deactivate();

        return toResponse(workflowRepository.save(workflow));
    }

    private WorkflowResponse toResponse(Workflow workflow) {
        return new WorkflowResponse(
                workflow.getId(),
                workflow.getName(),
                workflow.getDescription(),
                workflow.getStatus(),
                workflow.getCreatedAt(),
                workflow.getUpdatedAt(),
                workflow.getVersion()
        );
    }
}