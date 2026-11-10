-- Copenhagen Cupcakes: create tables and insert bottoms and toppings
-- Run the whole file in pgAdmin (Query Tool -> F5)

BEGIN;

-- Delete old tables first, so the script can be run again
DROP TABLE IF EXISTS orderlines, orders, toppings, bottoms, users;

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(255)   NOT NULL UNIQUE,
    password VARCHAR(255)   NOT NULL,
    balance  NUMERIC(10, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    role     VARCHAR(10)    NOT NULL DEFAULT 'customer' CHECK (role IN ('customer', 'admin'))
);

CREATE TABLE bottoms
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(50)   NOT NULL UNIQUE,
    price NUMERIC(5, 2) NOT NULL
);

CREATE TABLE toppings
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(50)   NOT NULL UNIQUE,
    price NUMERIC(5, 2) NOT NULL
);

CREATE TABLE orders
(
    id         SERIAL PRIMARY KEY,
    user_id    INT       NOT NULL REFERENCES users (id),
    order_date TIMESTAMP NOT NULL DEFAULT NOW(),
    paid       BOOLEAN   NOT NULL DEFAULT FALSE,
    picked_up  BOOLEAN   NOT NULL DEFAULT FALSE
);

CREATE TABLE orderlines
(
    id         SERIAL PRIMARY KEY,
    order_id   INT           NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    bottom_id  INT           NOT NULL REFERENCES bottoms (id),
    topping_id INT           NOT NULL REFERENCES toppings (id),
    quantity   INT           NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(6, 2) NOT NULL
);

INSERT INTO bottoms (name, price)
VALUES ('Vanilje', 15),
       ('Chokolade', 15),
       ('Citron', 16),
       ('Mandel', 17),
       ('Red velvet', 18);

INSERT INTO toppings (name, price)
VALUES ('Vaniljecreme', 12),
       ('Chokoladecreme', 14),
       ('Jordbær', 14),
       ('Hindbær', 14),
       ('Citron', 14),
       ('Mint', 14),
       ('Flødeost', 15),
       ('Karamel', 15),
       ('Pistacie', 16);

-- Test users (change the passwords if you like)
INSERT INTO users (email, password, balance, role)
VALUES ('admin@cupcake.dk', '1234', 0, 'admin'),
       ('kunde@mail.dk', '1234', 200, 'customer');

COMMIT;