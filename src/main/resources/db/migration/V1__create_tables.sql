CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL,
    added_at TIMESTAMP NOT NULL
);

CREATE TABLE arenas (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
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
    title VARCHAR(50) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    price NUMERIC(10, 2) NOT NULL DEFAULT 0,
    hall_id BIGINT NOT NULL REFERENCES halls(id),
    age_restrict INT NOT NULL DEFAULT 0
);

CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,
    row INT NOT NULL,
    seat_num INT NOT NULL,
    hall_id BIGINT NULL REFERENCES halls(id),

    CONSTRAINT uk_hall_row_seat UNIQUE (hall_id, row, seat_num)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR NOT NULL,
    email VARCHAR(30) UNIQUE NOT NULL,
    age INT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE tickets
(
    id         BIGSERIAL PRIMARY KEY,
    status     VARCHAR(10)   NOT NULL,
    event_id   BIGINT    NOT NULL REFERENCES events (id),
    seat_id    BIGINT    NOT NULL REFERENCES seats (id),
    user_id    BIGINT    NOT NULL REFERENCES users (id),
    created_at TIMESTAMP NOT NULL

    CONSTRAINT chk_ticket_status CHECK (status IN ('CHECKED', 'HELD', 'CANCELLED', 'CONFIRMED'))
);