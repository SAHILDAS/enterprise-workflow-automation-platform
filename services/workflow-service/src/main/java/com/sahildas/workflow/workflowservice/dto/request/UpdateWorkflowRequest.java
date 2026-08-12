package com.sahildas.workflow.workflowservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateWorkflowRequest(

        @NotBlank(message = "Workflow name is required")
        @Size(max = 200)
        String name,

        @Size(max = 5000)
        String description

) {}