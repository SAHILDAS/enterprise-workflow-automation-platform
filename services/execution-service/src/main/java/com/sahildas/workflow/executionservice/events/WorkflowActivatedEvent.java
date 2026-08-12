package com.sahildas.workflow.executionservice.events;

import java.time.Instant;
import java.util.UUID;

public record WorkflowActivatedEvent(
        UUID eventId,
        UUID workflowId,
        String workflowName,
        String status,
        Instant occurredAt
) {}