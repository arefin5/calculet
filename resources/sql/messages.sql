-- :name save-message! :! :n
-- :doc creates a new message using the name and message keys
INSERT INTO messages
(userid, message)
VALUES (:userid, :message)


-- :name get-messages :? :*
-- :doc selects all available messages
SELECT *
FROM   messages   m
       JOIN users u ON u.userid = m.userid
ORDER BY m.timestamp DESC,
         m.id DESC
