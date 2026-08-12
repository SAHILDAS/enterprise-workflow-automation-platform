CREATE TABLE workflow_executions (

    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    workflow_version_id UUID,

    status VARCHAR(30) NOT NULL,

    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    completed_at TIMESTAMP,

    initiated_by VARCHAR(100),

    input_payload JSONB,

    output_payload JSONB,

    error_message TEXT,

    version BIGINT NOT NULL DEFAULT 0

);

CREATE TABLE step_executions (

    id UUID PRIMARY KEY,

    workflow_execution_id UUID NOT NULL,

    step_name VARCHAR(200) NOT NULL,

    step_type VARCHAR(50) NOT NULL,

    sequence INTEGER NOT NULL,

    status VARCHAR(30) NOT NULL,

    started_at TIMESTAMP,

    completed_at TIMESTAMP,

    input_payload JSONB,

    output_payload JSONB,

    error_message TEXT,

    retry_count INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_step_execution_workflow_execution
        FOREIGN KEY (workflow_execution_id)
        REFERENCES workflow_executions(id)
        ON DELETE CASCADE

);

CREATE INDEX idx_workflow_executions_status
ON workflow_executions(status);

CREATE INDEX idx_workflow_executions_workflow
ON workflow_executions(workflow_id);

CREATE INDEX idx_step_executions_workflow_execution
ON step_executions(workflow_execution_id);

CREATE INDEX idx_step_executions_sequence
ON step_executions(workflow_execution_id, sequence);