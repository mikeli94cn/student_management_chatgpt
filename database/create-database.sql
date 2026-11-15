-- 1. Create database
CREATE DATABASE IF NOT EXISTS student_db_codex
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

-- 2. Create application user
CREATE USER IF NOT EXISTS 'student_app_codex'@'%'
IDENTIFIED BY '123456';

-- 3. Grant database permissions
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX
      ON student_db_codex.* TO 'student_app_codex'@'%';

-- 4. Verify the user
SELECT user, host
FROM mysql.user
WHERE user = 'student_app_codex';

-- 5. Verify privileges
SHOW GRANTS FOR 'student_app_codex'@'%';