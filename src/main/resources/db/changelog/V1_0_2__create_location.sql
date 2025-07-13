CREATE TABLE locations
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100)     NOT NULL,
    user_id    BIGINT           NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    latitude   DOUBLE PRECISION NOT NULL,
    longitude  DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP        NOT NULL
)