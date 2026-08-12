package com.sahildas.workflow.workflowservice.controller;

import com.sahildas.workflow.workflowservice.dto.request.CreateWorkflowRequest;
import com.sahildas.workflow.workflowservice.dto.request.UpdateWorkflowRequest;
import com.sahildas.workflow.workflowservice.dto.response.WorkflowResponse;
import com.sahildas.workflow.workflowservice.service.WorkflowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/workflows")
@Tag(name = "Workflows", description = "Workflow definition management APIs")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create workflow", description = "Create a new workflow definition")
    public WorkflowResponse createWorkflow(@Valid @RequestBody CreateWorkflowRequest request) {
        return workflowService.createWorkflow(request);
    }

    @GetMapping
    @Operation(summary = "List workflows", description = "Retrieve all workflow definitions")
    public List<WorkflowResponse> getAllWorkflows() {
        return workflowService.getAllWorkflows();
    }

    @GetMapping("/{workflowId}")
    @Operation(summary = "Get workflow by ID", description = "Retrieve a workflow by its unique identifier")
    public WorkflowResponse getWorkflow(@PathVariable("workflowId") UUID workflowId) {
        return workflowService.getWorkflow(workflowId);
    }

    @PutMapping("/{workflowId}")
    @Operation(summary = "Update workflow", description = "Update workflow name and description")
    public WorkflowResponse updateWorkflow(
            @PathVariable("workflowId") UUID workflowId,
            @Valid @RequestBody UpdateWorkflowRequest request
    ) {
        return workflowService.updateWorkflow(workflowId, request);
    }

    @PostMapping("/{workflowId}/activate")
    @Operation(summary = "Activate workflow", description = "Activate a workflow definition")
    public WorkflowResponse activateWorkflow(@PathVariable("workflowId") UUID workflowId) {
        return workflowService.activateWorkflow(workflowId);
    }

    @PostMapping("/{workflowId}/deactivate")
    @Operation(summary = "Deactivate workflow", description = "Deactivate a workflow definition")
    public WorkflowResponse deactivateWorkflow(@PathVariable("workflowId") UUID workflowId) {
        return workflowService.deactivateWorkflow(workflowId);
    }
}