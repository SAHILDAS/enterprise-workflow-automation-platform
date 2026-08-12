package com.sahildas.workflow.executionservice.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateExecutionRequest(

        @NotNull
        UUID workflowId,

        UUID workflowVersionId,

        String initiatedBy,

        String inputPayload

) {}