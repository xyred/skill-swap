-- 1. Insert Users
-- Added 'role' and 'tenant_id' to match your new Entity requirements
INSERT INTO users (username, email, password, credits, role, tenant_id)
VALUES ('StudentSam', 'sam@example.com', 'password123', 5, 'ROLE_USER', 'google');

INSERT INTO users (username, email, password, credits, role, tenant_id)
VALUES ('TeacherTom', 'tom@example.com', 'password456', 10, 'ROLE_TENANT_ADMIN', 'google');

-- 2. Insert Skills
-- Added 'tenant_id' here as well so the Skill is isolated to the same company
INSERT INTO skills (title, description, provider_id, tenant_id)
VALUES ('Java Programming',
        'Learn Spring Boot from scratch',
        (SELECT id FROM users WHERE email = 'tom@example.com'),
        'google');
