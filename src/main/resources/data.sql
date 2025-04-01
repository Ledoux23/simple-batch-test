CREATE TABLE IF NOT EXISTS members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL
);


INSERT INTO members (name, age) VALUES ('Alice', 25);
INSERT INTO members (name, age) VALUES ('Bob', 30);
INSERT INTO members (name, age) VALUES ('Charlie', 40);
INSERT INTO members (name, age) VALUES ('Jules', 43);
INSERT INTO members (name, age) VALUES ('Bernard', 20);
INSERT INTO members (name, age) VALUES ('Charlotte', 65);