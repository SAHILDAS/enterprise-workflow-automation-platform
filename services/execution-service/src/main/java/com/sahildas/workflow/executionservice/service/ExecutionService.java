package com.sahildas.workflow.executionservice.service;

import com.sahildas.workflow.executionservice.domain.WorkflowExecution;
import com.sahildas.workflow.executionservice.dto.request.CompleteExecutionRequest;
import com.sahildas.workflow.executionservice.dto.request.CreateExecutionRequest;
import com.sahildas.workflow.executionservice.dto.request.FailExecutionRequest;
import com.sahildas.workflow.executionservice.dto.response.WorkflowExecutionResponse;
import com.sahildas.workflow.executionservice.exception.ExecutionNotFoundException;
import com.sahildas.workflow.executionservice.repository.WorkflowExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExecutionService {

    private final WorkflowExecutionRepository workflowExecutionRepository;

    public ExecutionService(WorkflowExecutionRepository workflowExecutionRepository) {
        this.workflowExecutionRepository = workflowExecutionRepository;
    }

    public WorkflowExecutionResponse createExecution(CreateExecutionRequest request) {

        WorkflowExecution execution = new WorkflowExecution(
                request.workflowId(),
                request.workflowVersionId(),
                request.initiatedBy()
        );

        execution.setInputPayload(request.inputPayload());

        WorkflowExecution saved = workflowExecutionRepository.save(execution);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkflowExecutionResponse> getAllExecutions() {
        return workflowExecutionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkflowExecutionResponse getExecution(UUID executionId) {
        WorkflowExecution execution = workflowExecutionRepository.findById(executionId)
                .orElseThrow(() -> new ExecutionNotFoundException(executionId));

        return toResponse(execution);
    }

    public WorkflowExecutionResponse startExecution(UUID executionId) {
        WorkflowExecution execution = workflowExecutionRepository.findById(executionId)
                .orElseThrow(() -> new ExecutionNotFoundException(executionId));

        execution.start();

        return toResponse(workflowExecutionRepository.save(execution));
    }

    public WorkflowExecutionResponse completeExecution(
            UUID executionId,
            CompleteExecutionRequest request
    ) {
        WorkflowExecution execution = workflowExecutionRepository.findById(executionId)
                .orElseThrow(() -> new ExecutionNotFoundException(executionId));

        execution.complete(request.outputPayload());

        return toResponse(workflowExecutionRepository.save(execution));
    }

    public WorkflowExecutionResponse failExecution(
            UUID executionId,
            FailExecutionRequest request
    ) {
        WorkflowExecution execution = workflowExecutionRepository.findById(executionId)
                .orElseThrow(() -> new ExecutionNotFoundException(executionId));

        execution.fail(request.errorMessage());

        return toResponse(workflowExecutionRepository.save(execution));
    }

    private WorkflowExecutionResponse toResponse(WorkflowExecution execution) {
        return new WorkflowExecutionResponse(
                execution.getId(),
                execution.getWorkflowId(),
                execution.getWorkflowVersionId(),
                execution.getStatus(),
                execution.getStartedAt(),
                execution.getCompletedAt(),
                execution.getInitiatedBy(),
                execution.getInputPayload(),
                execution.getOutputPayload(),
                execution.getErrorMessage(),
                execution.getVersion()
        );
    }
}