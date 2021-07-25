CREATE TABLE IF NOT EXISTS users
(
userid        VARCHAR(30) PRIMARY KEY,
name          VARCHAR(100),
email_address VARCHAR(100),
password      VARCHAR(30),
timestamp     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--;;

CREATE TABLE IF NOT EXISTS messages
(
id          SERIAL,
userid      VARCHAR(30),
message     VARCHAR(200),
timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--;;

ALTER TABLE    messages
ADD CONSTRAINT user_msgs_fk1
FOREIGN KEY    (userid)
REFERENCES     users;

--;;

