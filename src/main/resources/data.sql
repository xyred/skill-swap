-- 1. Insert Users (Let the database handle the ID)
-- We omit 'id' so the IDENTITY generator stays in sync
INSERT INTO users (username, email, password, credits)
VALUES ('StudentSam', 'sam@example.com', 'password123', 5);

INSERT INTO users (username, email, password, credits)
VALUES ('TeacherTom', 'tom@example.com', 'password456', 10);

-- 2. Insert Skills
-- We use a Subquery to find the provider_id based on the email.
-- This is safer than guessing IDs, especially since they are now auto-generated!
INSERT INTO skills (title, description, provider_id)
VALUES ('Java Programming',
        'Learn Spring Boot from scratch',
        (SELECT id FROM users WHERE email = 'tom@example.com'));
