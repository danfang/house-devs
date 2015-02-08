CREATE TABLE user_messages (
    user_id varchar REFERENCES users(user_id),
    message_title text NOT NULL,
    message_body text NOT NULL
);
