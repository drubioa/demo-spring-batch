DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    person_id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    surname VARCHAR(20),
    age INT(3)
);

