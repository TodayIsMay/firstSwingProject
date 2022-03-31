CREATE SCHEMA `first_swing_db` ;
USE first_swing_db;
CREATE TABLE accounts (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR (200),
creation_time VARCHAR (200)
);
CREATE TABLE orders (
id INT PRIMARY KEY AUTO_INCREMENT,
account_id INT,
stock_name VARCHAR (5),
quantity INT,
ask_price INT,
creation_time VARCHAR (200)
);
CREATE TABLE symbols (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR (5),
ask INT,
bid INT);
INSERT INTO symbols (name, ask, bid)
VALUES ('AAPL', 170, 168);
INSERT INTO symbols (name, ask, bid)
VALUES ('FB', 220, 216);
INSERT INTO symbols (name, ask, bid)
VALUES ('AMZN', 3300, 3297);
INSERT INTO symbols (name, ask, bid)
VALUES ('YNDX', 20, 18);
INSERT INTO symbols (name, ask, bid)
VALUES ('TSLA', 1000, 993);