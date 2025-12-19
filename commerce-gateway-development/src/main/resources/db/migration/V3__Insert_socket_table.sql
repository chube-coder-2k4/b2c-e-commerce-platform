
Create table notification_socket (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(500) NOT NULL,
    read BOOLEAN DEFAULT false,
    reference_id VARCHAR(255),
    type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID,
    updated_by UUID,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);