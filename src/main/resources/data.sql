-- 1. CLEANUP (Optional, but safe with create-drop)
DELETE FROM skills;
DELETE FROM users;

-- 2. INSERT USERS (4 Tenants, 5 Users each)

-- TENANT: google
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
                                                                            ('google_hr', 'hr@google.com', 'password123', 'ROLE_TENANT_ADMIN', 'google', 100),
                                                                            ('sam_g', 'sam@google.com', 'password123', 'ROLE_USER', 'google', 5),
                                                                            ('beth_g', 'beth@google.com', 'password123', 'ROLE_USER', 'google', 5),
                                                                            ('rick_g', 'rick@google.com', 'password123', 'ROLE_USER', 'google', 5),
                                                                            ('morty_g', 'morty@google.com', 'password123', 'ROLE_USER', 'google', 5);

-- TENANT: meta
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
                                                                            ('meta_hr', 'hr@meta.com', 'password123', 'ROLE_TENANT_ADMIN', 'meta', 100),
                                                                            ('mark_m', 'mark@meta.com', 'password123', 'ROLE_USER', 'meta', 5),
                                                                            ('sheryl_m', 'sheryl@meta.com', 'password123', 'ROLE_USER', 'meta', 5),
                                                                            ('dustin_m', 'dustin@meta.com', 'password123', 'ROLE_USER', 'meta', 5),
                                                                            ('chris_m', 'chris@meta.com', 'password123', 'ROLE_USER', 'meta', 5);

-- TENANT: apple
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
                                                                            ('apple_hr', 'hr@apple.com', 'password123', 'ROLE_TENANT_ADMIN', 'apple', 100),
                                                                            ('tim_a', 'tim@apple.com', 'password123', 'ROLE_USER', 'apple', 5),
                                                                            ('steve_a', 'steve@apple.com', 'password123', 'ROLE_USER', 'apple', 5),
                                                                            ('jony_a', 'jony@apple.com', 'password123', 'ROLE_USER', 'apple', 5),
                                                                            ('phil_a', 'phil@apple.com', 'password123', 'ROLE_USER', 'apple', 5);

-- TENANT: amazon
INSERT INTO users (username, email, password, role, tenant_id, credits) VALUES
                                                                            ('amazon_hr', 'hr@amazon.com', 'password123', 'ROLE_TENANT_ADMIN', 'amazon', 100),
                                                                            ('jeff_z', 'jeff@amazon.com', 'password123', 'ROLE_USER', 'amazon', 5),
                                                                            ('andy_z', 'andy@amazon.com', 'password123', 'ROLE_USER', 'amazon', 5),
                                                                            ('werner_z', 'werner@amazon.com', 'password123', 'ROLE_USER', 'amazon', 5),
                                                                            ('mackenzie_z', 'mackenzie@amazon.com', 'password123', 'ROLE_USER', 'amazon', 5);

-- 3. INSERT SKILLS (10 Skills, assigned to different users/tenants)
-- Note: The tenant_id of the skill MUST match the tenant_id of the user (provider)

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