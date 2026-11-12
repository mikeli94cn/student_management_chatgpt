CREATE DATABASE IF NOT EXISTS student_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'student_app'@'localhost'
    IDENTIFIED BY 'change-me';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX
    ON student_db.* TO 'student_app'@'localhost';

FLUSH PRIVILEGES;
