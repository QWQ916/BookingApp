CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    added_at TIMESTAMP NOT NULL
);

CREATE TABLE arenas (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR,
    address VARCHAR(50) NOT NULL,
    city_id BIGINT NOT NULL REFERENCES cities(id),
    added_at TIMESTAMP NOT NULL,
    changed_at TIMESTAMP NOT NULL
);

CREATE TABLE halls (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR,
    arena_id BIGINT NOT NULL REFERENCES arenas(id),
    added_at TIMESTAMP NOT NULL,
    changed_at TIMESTAMP NOT NULL
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50),
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    price NUMERIC NOT NULL,
    hall_id BIGINT NOT NULL REFERENCES halls(id),
    age_restrict INT
);

CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,
    row INT UNIQUE NOT NULL,
    seat_num INT UNIQUE NOT NULL,
    hall_id BIGINT UNIQUE NULL REFERENCES halls(id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR NOT NULL,
    age INT NOT NULL
);

CREATE TABLE tickets
(
    id         BIGSERIAL PRIMARY KEY,
    status     VARCHAR   NOT NULL,
    event_id   BIGINT    NOT NULL REFERENCES events (id),
    seat_id    BIGINT    NOT NULL REFERENCES seats (id),
    user_id    BIGINT    NOT NULL REFERENCES users (id),
    created_at TIMESTAMP NOT NULL
);