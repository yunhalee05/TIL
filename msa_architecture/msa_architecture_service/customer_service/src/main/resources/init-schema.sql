DROP SCHEMA IF EXISTS `customer`;
CREATE SCHEMA `customer`;
USE
`customer`;

CREATE TABLE customers
(
    id         CHAR(36)     NOT NULL DEFAULT (UUID()),
    username   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);