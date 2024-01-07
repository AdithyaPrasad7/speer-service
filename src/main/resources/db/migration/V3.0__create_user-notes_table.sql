CREATE TABLE if NOT EXISTS user_notes(
    id bigserial PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES users(id),
    note_id bigint NOT NULL REFERENCES notes(id)
);