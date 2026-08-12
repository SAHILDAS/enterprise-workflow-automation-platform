package com.sahildas.workflow.workflowservice.service;

import com.sahildas.workflow.workflowservice.domain.Workflow;
import com.sahildas.workflow.workflowservice.dto.request.CreateWorkflowRequest;
import com.sahildas.workflow.workflowservice.dto.request.UpdateWorkflowRequest;
import com.sahildas.workflow.workflowservice.dto.response.WorkflowResponse;
import com.sahildas.workflow.workflowservice.events.WorkflowActivatedEvent;
import com.sahildas.workflow.workflowservice.exception.WorkflowAlreadyExistsException;
import com.sahildas.workflow.workflowservice.exception.WorkflowNotFoundException;
import com.sahildas.workflow.workflowservice.messaging.WorkflowEventPublisher;
import com.sahildas.workflow.workflowservice.repository.WorkflowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowEventPublisher workflowEventPublisher;

    public WorkflowService(
            WorkflowRepository workflowRepository,
            WorkflowEventPublisher workflowEventPublisher
    ) {
        this.workflowRepository = workflowRepository;
        this.workflowEventPublisher = workflowEventPublisher;
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

        // Update the existing entity
        workflow.setName(request.name());
        workflow.setDescription(request.description());

        Workflow saved = workflowRepository.save(workflow);

        return toResponse(saved);
    }

    public WorkflowResponse activateWorkflow(UUID workflowId) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowNotFoundException(workflowId));

        workflow.activate();

        Workflow saved = workflowRepository.save(workflow);

        // Publish Kafka event
        workflowEventPublisher.publishWorkflowActivated(
                new WorkflowActivatedEvent(
                        UUID.randomUUID(),
                        saved.getId(),
                        saved.getName(),
                        saved.getStatus().name(),
                        Instant.now()
                )
        );

        return toResponse(saved);
    }

    public WorkflowResponse deactivateWorkflow(UUID workflowId) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowNotFoundException(workflowId));

        workflow.deactivate();

        Workflow saved = workflowRepository.save(workflow);

        return toResponse(saved);
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