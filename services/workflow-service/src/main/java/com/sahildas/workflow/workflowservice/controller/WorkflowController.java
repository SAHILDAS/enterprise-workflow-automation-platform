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

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkflowResponse createWorkflow(@Valid @RequestBody CreateWorkflowRequest request) {
        return workflowService.createWorkflow(request);
    }

    @GetMapping
    public List<WorkflowResponse> getAllWorkflows() {
        return workflowService.getAllWorkflows();
    }

    @GetMapping("/{workflowId}")
    public WorkflowResponse getWorkflow(@PathVariable("workflowId") UUID workflowId) {
        return workflowService.getWorkflow(workflowId);
    }

    @PutMapping("/{workflowId}")
    public WorkflowResponse updateWorkflow(
            @PathVariable("workflowId") UUID workflowId,
            @Valid @RequestBody UpdateWorkflowRequest request
    ) {
        return workflowService.updateWorkflow(workflowId, request);
    }

    @PostMapping("/{workflowId}/activate")
    public WorkflowResponse activateWorkflow(@PathVariable("workflowId") UUID workflowId) {
        return workflowService.activateWorkflow(workflowId);
    }

    @PostMapping("/{workflowId}/deactivate")
    public WorkflowResponse deactivateWorkflow(@PathVariable("workflowId") UUID workflowId) {
        return workflowService.deactivateWorkflow(workflowId);
    }
}