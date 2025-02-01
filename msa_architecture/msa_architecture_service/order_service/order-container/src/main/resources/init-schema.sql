DROP SCHEMA IF EXISTS `order`;
CREATE SCHEMA `order`;
USE `order`;

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS order_address;
DROP TYPE IF EXISTS order_status;

CREATE TABLE orders
(
    id               CHAR(36)       NOT NULL DEFAULT (UUID()),
    customer_id      CHAR(36)       NOT NULL,
    restaurant_id    CHAR(36)       NOT NULL,
    tracking_id      CHAR(36)       NOT NULL,
    price            DECIMAL(10, 2) NOT NULL,
    order_status     ENUM('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING') NOT NULL,
    failure_messages VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE order_items
(
    id         BIGINT         NOT NULL,
    order_id   CHAR(36)       NOT NULL,
    product_id CHAR(36)       NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    quantity   INT            NOT NULL,
    sub_total  DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id, order_id),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

CREATE TABLE order_address
(
    id          CHAR(36)     NOT NULL DEFAULT (UUID()),
    order_id    CHAR(36)     NOT NULL UNIQUE,
    street      VARCHAR(255) NOT NULL,
    postal_code VARCHAR(20)  NOT NULL,
    city        VARCHAR(100) NOT NULL,
    PRIMARY KEY (id, order_id),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);