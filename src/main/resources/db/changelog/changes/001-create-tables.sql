--liquibase formatted sql

--changeset nazri:001-create-books
CREATE TABLE books (
                       id               BIGSERIAL PRIMARY KEY,
                       title            VARCHAR(255) NOT NULL,
                       author           VARCHAR(255) NOT NULL,
                       isbn             VARCHAR(20)  NOT NULL UNIQUE,
                       total_copies     INTEGER      NOT NULL,
                       available_copies INTEGER      NOT NULL,
                       CONSTRAINT chk_books_copies CHECK (available_copies >= 0 AND available_copies <= total_copies)
);

--changeset nazri:002-create-members
CREATE TABLE members (
                         id        BIGSERIAL PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL,
                         email     VARCHAR(255) NOT NULL UNIQUE
);

--changeset nazri:003-create-loans
CREATE TABLE loans (
                       id          BIGSERIAL PRIMARY KEY,
                       book_id     BIGINT      NOT NULL REFERENCES books(id),
                       member_id   BIGINT      NOT NULL REFERENCES members(id),
                       loan_date   DATE        NOT NULL,
                       return_date DATE,
                       status      VARCHAR(20) NOT NULL
);
CREATE INDEX idx_loans_member_status ON loans(member_id, status);