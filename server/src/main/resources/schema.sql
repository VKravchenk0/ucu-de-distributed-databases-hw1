CREATE TABLE IF NOT EXISTS user_counter (
    user_id BIGINT PRIMARY KEY,
    counter BIGINT NOT NULL,
    version INTEGER NOT NULL
);