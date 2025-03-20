-- Evitar duplicados
DROP SCHEMA IF EXISTS users CASCADE;

-- Extensión para crear identificadores UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Esquema del usuario
CREATE SCHEMA users;

CREATE TABLE IF NOT EXISTS users.UserTable (
    user_id         UUID DEFAULT uuid_generate_v4(),
    name            VARCHAR(50) NOT NULL,
    surname         VARCHAR(50),
    gender          VARCHAR(10) NOT NULL,
    birthDate       DATE        NOT NULL,
    registeredAt    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_UserTable PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS users.Credential (
    credential_id       SERIAL,
    nickname            VARCHAR(50) NOT NULL,
    passwordEncrypted   VARCHAR     NOT NULL,
    user_id             UUID        NOT NULL,

    CONSTRAINT PK_Credential PRIMARY KEY (credential_id),
    CONSTRAINT FK_Credential_TO_UserTable
        FOREIGN KEY (user_id) REFERENCES users.UserTable (user_id)
            ON UPDATE CASCADE,
    CONSTRAINT UNIQUE_Credential_nickname UNIQUE (nickname)
);

CREATE TABLE IF NOT EXISTS users.Role (
    role_id     SERIAL,
    name        VARCHAR NOT NULL,

    CONSTRAINT PK_Role PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS users.RoleAssignment (
    user_id         UUID,
    role_id         INTEGER,
    assignedAt      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_RoleAssignment PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_RoleAssignment_TO_UserTable
        FOREIGN KEY (user_id) REFERENCES users.UserTable (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT FK_RoleAssignment_TO_Role
        FOREIGN KEY (role_id) REFERENCES users.Role (role_id)
            ON UPDATE CASCADE
);

INSERT INTO users.Role(role) VALUES ('ADMIN');
INSERT INTO users.Role(role) VALUES ('BASIC');
INSERT INTO users.Role(role) VALUES ('PREMIUM');

CREATE TABLE IF NOT EXISTS users.ContactInfo (
    contactInfo_id          SERIAL,
    email                   VARCHAR(100) NOT NULL,
    isEmailVerified         BOOLEAN      NOT NULL DEFAULT FALSE,
    phoneNumber             VARCHAR(20)  NOT NULL,
    isPhoneNumberVerified   BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id                 UUID         NOT NULL,

    CONSTRAINT PK_ContactInfo PRIMARY KEY (contactInfo_id),
    CONSTRAINT FK_ContactInfo_TO_UserTable
        FOREIGN KEY (user_id) REFERENCES users.UserTable (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT UNIQUE_ContactInfo_email UNIQUE (email)
);

