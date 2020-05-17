(ns luminapp.test.db.core
  (:require
    [luminapp.db.core :refer [*db*] :as db]
    [java-time.pre-java8]
    [luminus-migrations.core :as migrations]
    [clojure.test :refer :all]
    [clojure.java.jdbc :as jdbc]
    [luminapp.config :refer [env]]
    [mount.core :as mount]
    ))


;; use-fixtures
;; ------------
;; Wrap test runs in a fixture function to perform setup and
;; teardown. Using a fixture-type of :each wraps every test
;; individually, while :once wraps the whole run in a single function.
(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'luminapp.config/env
      #'luminapp.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))


(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
                            (jdbc/db-set-rollback-only! t-conn)
                            (is (= 1 (db/create-user!
                                       t-conn
                                       {:userid        "Bob"
                                        :name          "Bob Blob"
                                        :email_address "bob.blob@blobbob.co.uk"
                                        :password      "F1shAndCh1p5"}
                                       {:connection t-conn})))
                            (is (= {:record_found "Y"}
                                   (db/authenticate t-conn {:userid "Bob" :password "F1shAndCh1p5"})))))

(deftest test-messages
  (jdbc/with-db-transaction [t-conn *db*]
                            (jdbc/db-set-rollback-only! t-conn)
                            (is (= 1 (db/save-message!
                                       t-conn
                                       {:userid  "Bob"
                                        :message "Hello, World"}
                                       {:connection t-conn})))
                            (is (= {:userid  "Bob"
                                    :message "Hello, World"}
                                   (-> (db/get-messages t-conn {})
                                       (first)
                                       (select-keys [:userid :message]))))))

