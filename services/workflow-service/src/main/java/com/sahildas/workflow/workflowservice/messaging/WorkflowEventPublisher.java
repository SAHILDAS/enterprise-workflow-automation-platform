package com.sahildas.workflow.workflowservice.messaging;

import com.sahildas.workflow.workflowservice.events.WorkflowActivatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkflowEventPublisher {

    private final KafkaTemplate<String, WorkflowActivatedEvent> kafkaTemplate;

    @Value("${workflow.kafka.topics.lifecycle-events}")
    private String lifecycleTopic;

    public WorkflowEventPublisher(
            KafkaTemplate<String, WorkflowActivatedEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishWorkflowActivated(WorkflowActivatedEvent event) {

        kafkaTemplate.send(
                lifecycleTopic,
                event.workflowId().toString(),
                event
        );
    }
}