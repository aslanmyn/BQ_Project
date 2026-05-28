CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE users (
    id              UUID        PRIMARY KEY,
    email           CITEXT      NOT NULL UNIQUE,
    username        CITEXT      NOT NULL UNIQUE,
    password_hash   TEXT        NOT NULL,
    email_verified  BOOLEAN     NOT NULL DEFAULT FALSE,
    display_name    TEXT,
    profile_image_url TEXT,
    created_at      TIMESTAMPTZ NOT NULL,
    updated_at      TIMESTAMPTZ NOT NULL
);

CREATE TABLE email_verifications (
    id          UUID        PRIMARY KEY,
    user_id     UUID        NOT NULL REFERENCES users(id),
    code_hash   TEXT        NOT NULL,
    purpose     TEXT        NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    consumed_at TIMESTAMPTZ,
    attempts    INT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_ev_user_purpose ON email_verifications (user_id, purpose, expires_at DESC);

CREATE TABLE refresh_tokens (
    id          UUID        PRIMARY KEY,
    user_id     UUID        NOT NULL REFERENCES users(id),
    token_hash  TEXT        NOT NULL UNIQUE,
    device_id   TEXT,
    expires_at  TIMESTAMPTZ NOT NULL,
    revoked_at  TIMESTAMPTZ
);

CREATE INDEX idx_rt_user_id ON refresh_tokens (user_id);
