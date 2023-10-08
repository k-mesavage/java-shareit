DROP TABLE IF EXISTS users, items, bookings, requests, comments;

CREATE TABLE IF NOT EXISTS
    users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  varchar(46),
    email varchar(46),
    CONSTRAINT unique_email UNIQUE (email)
    );
CREATE TABLE IF NOT EXISTS
    requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  varchar(200),
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    creation_date TIMESTAMP WITHOUT TIME ZONE
    );
CREATE TABLE IF NOT EXISTS
    items
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         varchar(46),
    description  varchar(200),
    is_available boolean,
    owner_id     BIGINT REFERENCES users (id) ON DELETE CASCADE,
    request_id   int REFERENCES requests (id) ON DELETE CASCADE
    );
CREATE TABLE IF NOT EXISTS
    bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT REFERENCES items (id) ON DELETE CASCADE,
    booker_id  BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status     varchar(12)
    );
CREATE TABLE IF NOT EXISTS
    comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text      varchar(2000),
    item_id   BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created   TIMESTAMP WITHOUT TIME ZONE
    );
CREATE INDEX index_users ON users(id, name, email);
CREATE INDEX index_requests ON requests(id, description, requester_id);
CREATE INDEX index_items ON items(id, name, description, is_available, owner_id, request_id);
CREATE INDEX index_bookings ON bookings(id, start_date, end_date, item_id, booker_id, status);
CREATE INDEX index_comments ON comments(id, text, item_id, author_id, created);