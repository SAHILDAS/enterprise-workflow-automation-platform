package com.sahildas.workflow.workflowservice.dto.response;

import com.sahildas.workflow.workflowservice.domain.enums.WorkflowStatus;
import java.time.Instant;
import java.util.UUID;

public record WorkflowResponse(

        UUID id,

        String name,

        String description,

        WorkflowStatus status,

        Instant createdAt,

        Instant updatedAt,

        Long version

) {}