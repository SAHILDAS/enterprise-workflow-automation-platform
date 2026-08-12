CREATE TABLE workflows (

    id UUID PRIMARY KEY,

    name VARCHAR(200) NOT NULL,

    description TEXT,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    version BIGINT NOT NULL DEFAULT 0

);

CREATE TABLE workflow_versions (

    id UUID PRIMARY KEY,

    workflow_id UUID NOT NULL,

    version_number INTEGER NOT NULL,

    status VARCHAR(30) NOT NULL,

    definition JSONB,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_by VARCHAR(100),

    CONSTRAINT fk_workflow_version_workflow
        FOREIGN KEY (workflow_id)
        REFERENCES workflows(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_workflow_version
        UNIQUE (workflow_id, version_number)

);

CREATE TABLE workflow_steps (

    id UUID PRIMARY KEY,

    workflow_version_id UUID NOT NULL,

    name VARCHAR(200) NOT NULL,

    step_type VARCHAR(50) NOT NULL,

    sequence INTEGER NOT NULL,

    configuration JSONB,

    retry_policy JSONB,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_workflow_step_version
        FOREIGN KEY (workflow_version_id)
        REFERENCES workflow_versions(id)
        ON DELETE CASCADE

);

CREATE INDEX idx_workflows_status
ON workflows(status);

CREATE INDEX idx_workflow_versions_workflow
ON workflow_versions(workflow_id);

CREATE INDEX idx_workflow_steps_version
ON workflow_steps(workflow_version_id);

CREATE INDEX idx_workflow_steps_sequence
ON workflow_steps(workflow_version_id, sequence);