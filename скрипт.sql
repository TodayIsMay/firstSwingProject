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