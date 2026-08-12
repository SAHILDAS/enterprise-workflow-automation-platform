package com.sahildas.workflow.executionservice.dto.response;

import com.sahildas.workflow.executionservice.domain.enums.ExecutionStatus;

import java.time.Instant;
import java.util.UUID;

public record WorkflowExecutionResponse(

        UUID id,

        UUID workflowId,

        UUID workflowVersionId,

        ExecutionStatus status,

        Instant startedAt,

        Instant completedAt,

        String initiatedBy,

        String inputPayload,

        String outputPayload,

        String errorMessage,

        Long version

) {}