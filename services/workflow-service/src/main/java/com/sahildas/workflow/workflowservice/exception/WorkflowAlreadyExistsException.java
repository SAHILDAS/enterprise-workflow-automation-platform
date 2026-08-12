package com.sahildas.workflow.workflowservice.exception;

public class WorkflowAlreadyExistsException extends RuntimeException {

    public WorkflowAlreadyExistsException(String name) {
        super("Workflow already exists: " + name);
    }

}