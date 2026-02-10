-- 1. Insert Users (Must match all @Column constraints)
-- We provide id, username, email, password, and credits
INSERT INTO users (id, username, email, password, credits)
VALUES (1, 'StudentSam', 'sam@example.com', 'password123', 5);

INSERT INTO users (id, username, email, password, credits)
VALUES (2, 'TeacherTom', 'tom@example.com', 'password456', 10);

-- 2. Insert Skills
-- Note: the table name is "skills" (plural) as per your @Table(name = "skills")
INSERT INTO skills (id, title, description, provider_id)
VALUES (100, 'Java Programming', 'Learn Spring Boot from scratch', 2);
