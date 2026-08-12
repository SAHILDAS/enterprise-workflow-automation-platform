package com.sahildas.workflow.executionservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FailExecutionRequest(

        @NotBlank
        String errorMessage

) {}