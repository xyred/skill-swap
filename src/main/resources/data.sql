-- 1. CLEANUP
DELETE FROM swap_transactions;
DELETE FROM skills;
DELETE FROM users;

-- 2. INSERT USERS (All passwords are 'password123')

-- THE SYSTEM ADMIN
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
('super_admin', 'admin@skillswap.io', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_SUPER_ADMIN', 'system', 9999);

-- TENANT: google
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
('google_hr', 'hr@google.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_TENANT_ADMIN', 'google', 100),
('sam_g', 'sam@google.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'google', 5),
('beth_g', 'beth@google.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'google', 5),
('rick_g', 'rick@google.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'google', 5),
('morty_g', 'morty@google.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'google', 5);

-- TENANT: meta
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
('meta_hr', 'hr@meta.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_TENANT_ADMIN', 'meta', 100),
('mark_m', 'mark@meta.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'meta', 5),
('sheryl_m', 'sheryl@meta.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'meta', 5),
('dustin_m', 'dustin@meta.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'meta', 5),
('chris_m', 'chris@meta.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'meta', 5);

-- TENANT: apple
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
('apple_hr', 'hr@apple.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_TENANT_ADMIN', 'apple', 100),
('tim_a', 'tim@apple.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'apple', 5),
('steve_a', 'steve@apple.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'apple', 5),
('jony_a', 'jony@apple.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'apple', 5),
('phil_a', 'phil@apple.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'apple', 5);

-- TENANT: amazon
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
('amazon_hr', 'hr@amazon.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_TENANT_ADMIN', 'amazon', 100),
('jeff_z', 'jeff@amazon.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'amazon', 5),
('andy_z', 'andy@amazon.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'amazon', 5),
('werner_z', 'werner@amazon.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'amazon', 5),
('mackenzie_z', 'mackenzie@amazon.com', '$2a$12$OCIEFCxeT7U6x4g/3gF2xe1/RvcaVWk3TiWZPeY3Ls3nbuk9e9hqW', 'ROLE_USER', 'amazon', 5);

-- 3. INSERT SKILLS
INSERT INTO skills (title, description, tenant_id, provider_id) VALUES
('Java Backend', 'Spring Boot expert', 'google', (SELECT id FROM users WHERE username = 'sam_g')),
('Frontend React', 'Modern UI/UX', 'google', (SELECT id FROM users WHERE username = 'beth_g')),
('Python Scripting', 'Automation tools', 'meta', (SELECT id FROM users WHERE username = 'mark_m')),
('Data Science', 'Machine learning models', 'meta', (SELECT id FROM users WHERE username = 'dustin_m')),
('Swift Development', 'iOS app master', 'apple', (SELECT id FROM users WHERE username = 'tim_a')),
('Hardware Design', 'Chip architecture', 'apple', (SELECT id FROM users WHERE username = 'steve_a')),
('AWS Cloud', 'Infrastructure as code', 'amazon', (SELECT id FROM users WHERE username = 'jeff_z')),
('Logistics Ops', 'Supply chain optimization', 'amazon', (SELECT id FROM users WHERE username = 'andy_z')),
('Cyber Security', 'Penetration testing', 'google', (SELECT id FROM users WHERE username = 'rick_g')),
('Product Design', 'User research and Figma', 'meta', (SELECT id FROM users WHERE username = 'chris_m'));