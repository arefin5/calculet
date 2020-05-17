(ns luminapp.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [luminapp.handler :refer :all]
    [luminapp.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]

    [ring.middleware.session :as session]
    ))

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

  (testing "authenticate ok"
    (println "test startes")
    ;    (let [response ((app)
    (let [response ((session/wrap-session (app))
                     (request :post "/login" {:userid "peter" :password "piper"}))
          {status :status headers :headers body :body session :session} response]
      (is (= [302 "" "http://localhost/"] [status body (get headers "Location")]))))

  (testing "authenticate fail"
    (let [response ((app)
                    (request :post "/login" {:userid "peter" :password "poper"}))
          {status :status headers :headers body :body} response]
      (is (= [302 "" "http://localhost/login"] [status body (get headers "Location")]))))

  )
