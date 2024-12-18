CREATE TABLE IF NOT EXISTS diary (
    id SERIAL PRIMARY KEY,
    topic VARCHAR(255),
    description VARCHAR(255)
);