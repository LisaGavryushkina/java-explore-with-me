    DROP TABLE IF EXISTS compilations_and_events;
    DROP TABLE IF EXISTS compilations;
    DROP TABLE IF EXISTS requests;
    DROP TABLE IF EXISTS events;
    DROP TABLE IF EXISTS categories;
    DROP TABLE IF EXISTS users;

    CREATE TABLE IF NOT EXISTS users (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
      name VARCHAR(255) NOT NULL,
      email VARCHAR(512) NOT NULL,
      CONSTRAINT pk_user PRIMARY KEY (id),
      CONSTRAINT uq_user_email UNIQUE (email),
      CONSTRAINT uq_user_name UNIQUE (name)
    );

    CREATE TABLE IF NOT EXISTS categories (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
      name VARCHAR(50) NOT NULL,
      CONSTRAINT pk_category PRIMARY KEY (id),
      CONSTRAINT uq_category_name UNIQUE (name)
    );

    CREATE TABLE IF NOT EXISTS events (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
      annotation VARCHAR(2000) NOT NULL,
      category_id BIGINT NOT NULL,
      confirmed_requests BIGINT NOT NULL,
      created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      description VARCHAR(7000) NOT NULL,
      event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      initiator_id BIGINT NOT NULL,
      latitude REAL NOT NULL,
      longitude REAL NOT NULL,
      is_paid BOOLEAN NOT NULL,
      participant_limit BIGINT NOT NULL,
      published TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      request_moderation BOOLEAN NOT NULL,
      state VARCHAR(255) NOT NULL,
      title VARCHAR(255) NOT NULL,
      views BIGINT NOT NULL,
      CONSTRAINT pk_event PRIMARY KEY (id),
      CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
      CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id)
    );


    CREATE TABLE IF NOT EXISTS requests (
       id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
       created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       event_id BIGINT NOT NULL,
       requester_id BIGINT NOT NULL,
       status VARCHAR(255) NOT NULL,
       CONSTRAINT pk_request PRIMARY KEY (id),
       CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
       CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id)
    );


    CREATE TABLE IF NOT EXISTS compilations (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
      is_pinned BOOLEAN NOT NULL,
      title VARCHAR(255) NOT NULL,
      CONSTRAINT pk_compilation PRIMARY KEY (id),
      CONSTRAINT uq_compilation_title UNIQUE (title)
    );

    CREATE TABLE IF NOT EXISTS compilations_and_events (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
      compilation_id BIGINT NOT NULL,
      event_id BIGINT NOT NULL,
      CONSTRAINT pk_compilation_and_event PRIMARY KEY (id),
      CONSTRAINT fk_compilations_and_events_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(id),
      CONSTRAINT fk_compilations_and_events_to_events FOREIGN KEY(event_id) REFERENCES events(id)
    );

