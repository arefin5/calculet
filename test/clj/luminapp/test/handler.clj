(ns luminapp.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [luminapp.handler :refer :all]
    [luminapp.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'luminapp.config/env
                 #'luminapp.handler/app-routes)
    (f)))

(deftest test-app

  (testing "login"
    (let [response ((app) (request :get "/login"))]
      (is (= 200 (:status response)))))

  ;(testing "main route"
  ;  (let [response ((app) (request :get "/"))]
  ;    (is (= 200 (:status response)))))

  ;(testing "not-found route"
  ;  (let [response ((app) (request :get "/invalid"))]
  ;    (is (= 404 (:status response)))))

  )
