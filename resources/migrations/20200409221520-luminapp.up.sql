CREATE TABLE messages
(
id          INTEGER PRIMARY KEY AUTO_INCREMENT,
userid      VARCHAR(30),
message     VARCHAR(200),
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE users
(
userid        VARCHAR(30) PRIMARY KEY,
name          VARCHAR(100),
email_address VARCHAR(100),
password      VARCHAR(30),
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

