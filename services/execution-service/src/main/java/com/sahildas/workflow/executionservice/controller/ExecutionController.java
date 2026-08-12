package com.sahildas.workflow.executionservice.controller;

import com.sahildas.workflow.executionservice.dto.request.CompleteExecutionRequest;
import com.sahildas.workflow.executionservice.dto.request.CreateExecutionRequest;
import com.sahildas.workflow.executionservice.dto.request.FailExecutionRequest;
import com.sahildas.workflow.executionservice.dto.response.WorkflowExecutionResponse;
import com.sahildas.workflow.executionservice.service.ExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/executions")
@Tag(name = "Executions", description = "Workflow execution management APIs")
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create execution", description = "Create a new workflow execution instance")
    public WorkflowExecutionResponse createExecution(
            @Valid @RequestBody CreateExecutionRequest request
    ) {
        return executionService.createExecution(request);
    }

    @GetMapping
    @Operation(summary = "List executions", description = "Retrieve all workflow executions")
    public List<WorkflowExecutionResponse> getAllExecutions() {
        return executionService.getAllExecutions();
    }

    @GetMapping("/{executionId}")
    @Operation(summary = "Get execution by ID", description = "Retrieve a workflow execution by its unique identifier")
    public WorkflowExecutionResponse getExecution(
            @PathVariable("executionId") UUID executionId
    ) {
        return executionService.getExecution(executionId);
    }

    @PostMapping("/{executionId}/start")
    @Operation(summary = "Start execution", description = "Transition an execution to RUNNING state")
    public WorkflowExecutionResponse startExecution(
            @PathVariable("executionId") UUID executionId
    ) {
        return executionService.startExecution(executionId);
    }

    @PostMapping("/{executionId}/complete")
    @Operation(summary = "Complete execution", description = "Mark an execution as COMPLETED")
    public WorkflowExecutionResponse completeExecution(
            @PathVariable("executionId") UUID executionId,
            @RequestBody CompleteExecutionRequest request
    ) {
        return executionService.completeExecution(executionId, request);
    }

    @PostMapping("/{executionId}/fail")
    @Operation(summary = "Fail execution", description = "Mark an execution as FAILED")
    public WorkflowExecutionResponse failExecution(
            @PathVariable("executionId") UUID executionId,
            @Valid @RequestBody FailExecutionRequest request
    ) {
        return executionService.failExecution(executionId, request);
    }
}