package com.sahildas.workflow.workflowservice.messaging;

import com.sahildas.workflow.workflowservice.events.WorkflowActivatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkflowEventPublisher {

private static final Logger log = LoggerFactory.getLogger(WorkflowEventPublisher.class);

private final KafkaTemplate<String, WorkflowActivatedEvent> kafkaTemplate;

@Value("${workflow.kafka.topics.lifecycle-events}")
private String lifecycleTopic;

public WorkflowEventPublisher(KafkaTemplate<String, WorkflowActivatedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
}

public void publishWorkflowActivated(WorkflowActivatedEvent event) {
    try {
        kafkaTemplate.send(
                lifecycleTopic,
                event.workflowId().toString(),
                event
        );

        log.info("Published workflow activation event for workflowId={} to topic={}",
                event.workflowId(), lifecycleTopic);

    } catch (Exception ex) {
        log.warn(
                "Kafka unavailable; workflow activation event was not published. " +
                "workflowId={}, reason={}",
                event.workflowId(),
                ex.getMessage()
        );
    }
}

}
