
-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(userid, name, email_address, password)
VALUES (:userid, :name, :email_address, :password)


-- :name get-users :? :*
-- :doc returns all user records
SELECT * FROM users


-- :name get-user :? :1
-- :doc Authenticate against the user table
SELECT *
FROM   users
WHERE  userid = :userid
