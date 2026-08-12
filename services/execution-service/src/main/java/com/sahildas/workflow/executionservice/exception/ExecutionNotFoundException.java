package com.sahildas.workflow.executionservice.exception;

import java.util.UUID;

public class ExecutionNotFoundException extends RuntimeException {

    public ExecutionNotFoundException(UUID executionId) {
        super("Execution not found: " + executionId);
    }

}