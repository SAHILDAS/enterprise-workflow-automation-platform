package com.sahildas.workflow.executionservice.messaging;

import com.sahildas.workflow.executionservice.events.WorkflowActivatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
name = "workflow.kafka.consumer.enabled",
havingValue = "true",
matchIfMissing = true
)
public class WorkflowLifecycleConsumer {

private static final Logger log =
        LoggerFactory.getLogger(WorkflowLifecycleConsumer.class);

@KafkaListener(
        topics = "${workflow.kafka.topics.lifecycle-events}",
        groupId = "execution-service"
)
public void consumeWorkflowActivated(WorkflowActivatedEvent event) {

    log.info(
            "Workflow ACTIVATED event received: workflowId={}, workflowName={}, occurredAt={}",
            event.workflowId(),
            event.workflowName(),
            event.occurredAt()
    );

    // Phase 5 MVP:
    // We only log the event.
    // Phase 6:
    // Automatically create WorkflowExecution records.
}

}
