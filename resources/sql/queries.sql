-- :name save-message! :! :n
-- :doc creates a new message using the name and message keys
INSERT INTO messages
(userid, message)
VALUES (:userid, :message)


-- :name get-messages :? :*
-- :doc selects all available messages
SELECT * from messages


-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(userid, name, email_address, password)
VALUES (:userid, :name, :email_address, :password)


-- :name authenticate :? :1
-- :doc Authenticate against the user table
SELECT 'Y' as record_found
FROM   users
WHERE  userid = :userid
AND    password = :password

