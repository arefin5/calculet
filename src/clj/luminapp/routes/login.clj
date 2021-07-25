(ns luminapp.routes.login
  (:require
    [luminapp.layout :as layout]
    [luminapp.db.core :as db]
    ;    [clojure.java.io :as io]
    [luminapp.middleware :as middleware]
    [ring.util.response :refer [redirect]]
    [ring.util.http-response :as response]
    [struct.core :as st]))


(defn- login-page [request]
  (->
    (layout/render request "login.html")
    (assoc :session nil)))


(defn- authenticate [{:keys [params]}]
  (let [user (db/get-user params)]
    (if (= (:password params) (:password user))
      (merge
        (response/found "/")
        {:session {:authenticated? true
                   :userid (:userid params)
                   :name (:name user)}})
      (response/found "/login"))))


(defn- register-page [request]
  (->
    (layout/render request "register.html")))


(defn- register [{:keys [params]}]
  (db/create-user! params)
  (response/found "/login"))


(defn login-routes []
  [""
   ["/login" {:get  login-page
              :post authenticate}]                         ;; not protected by the wrap-login middleware
   ["/register" {:get  register-page
                 :post register}]                          ;; not protected by the wrap-login middleware
   ["/logout" {:get (fn [_] (response/found "/login"))}]
   ])

