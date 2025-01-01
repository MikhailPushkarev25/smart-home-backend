-- Version 1.0.0: Initial schema for the Smart Home project

-- Table for storing user information
CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing devices
CREATE TABLE devices
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    type         VARCHAR(50)  NOT NULL,
    status       BOOLEAN   DEFAULT false,
    room         VARCHAR(255),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing automation rules
CREATE TABLE automations
(
    id         BIGSERIAL PRIMARY KEY,
    device_id  INT REFERENCES devices (id) ON DELETE CASCADE,
    rule       JSONB NOT NULL,
    is_active  BOOLEAN   DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing notifications
CREATE TABLE notifications
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    device_id  INT REFERENCES devices (id) ON DELETE CASCADE,
    message    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing device metrics
CREATE TABLE device_metrics
(
    id          BIGSERIAL PRIMARY KEY,
    device_id   INT REFERENCES devices (id) ON DELETE CASCADE,
    metric_type VARCHAR(50) NOT NULL,
    value       NUMERIC     NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance optimization
CREATE INDEX idx_device_type ON devices (type);
CREATE INDEX idx_automation_rule ON automations USING GIN(rule);
CREATE INDEX idx_device_metrics_recorded_at ON device_metrics (recorded_at);
