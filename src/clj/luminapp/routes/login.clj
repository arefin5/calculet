(ns luminapp.routes.login
  (:require
    [luminapp.layout :as layout]
    [luminapp.db.core :as db]
    ;    [clojure.java.io :as io]
    [luminapp.middleware :as middleware]
    [ring.util.response :refer [redirect]]
    [ring.util.http-response :as response]
    [struct.core :as st]))


(defn login-page [request]
  (->
    (layout/render request "login.html")
    (assoc :session nil)))


(defn register-page [request]
  (->
    (layout/render request "register.html")))


(defn authenticate [{:keys [params]}]
  (if (= (:password params) "piper")
    ;(assoc-in
    ;  (response/found "/")
    ;  [:session :authenticated?] true)
    (merge
      (response/found "/")
      {:session {:authenticated? true}})
    (response/found "/login")))


(defn register [{:keys [params]}]
  (db/create-user! params)
  (response/found "/login"))


(defn login-routes []
  [""
   ["/login" {:get  login-page
              :post authenticate}]                         ;; not protected by the wrap-login middleware
   ["/register" {:get  register-page
                 :post register}]                          ;; not protected by the wrap-login middleware
   ["/logout" {:get (fn [x] (response/found "/login"))}]
   ]
  )

