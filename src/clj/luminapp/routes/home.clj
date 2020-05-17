(ns luminapp.routes.home
  (:require
    [luminapp.layout :as layout]
    [luminapp.db.core :as db]
    ;    [clojure.java.io :as io]
    [luminapp.middleware :as middleware]
    [ring.util.response :refer [redirect]]
    [ring.util.http-response :as response]
    [struct.core :as st]))


(def message-schema
  [[:name
    st/required
    st/string]
   [:message
    st/required
    st/string
    {:message  "message must contain at least 10 characters"
     :validate (fn [msg] (>= (count msg) 10))}]])


(defn validate-message [params]
  (first (st/validate params message-schema)))


(defn home-page [{:keys [flash] :as request}]
  (layout/render
    request
    "home.html"
    (merge {:messages (db/get-messages)}
           (select-keys flash [:name :message :errors]))))


(defn save-message! [{:keys [params]}]
  ;  (db/save-message! params)
  ;  (response/found "/"))
  (if-let [errors (validate-message params)]
    (-> (response/found "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/save-message! params)
      (response/found "/"))))


(defn about-page [request]
  (layout/render request "about.html"))


(defn login-page [request]
  (->
    (layout/render request "login.html")
    (assoc :session nil)))


(defn register-page [request]
  (->
    (layout/render request "register.html")))


(defn authenticate [{:keys [params]}]
  (if (= (:password params) "piper")
    (merge
      (response/found "/")
      {:session {:authenticated? true}})
    (response/found "/login")))


(defn register [{:keys [params]}]
  (response/found "/login"))


(defn home-routes []
   [""
    {:middleware [middleware/wrap-login
                  middleware/wrap-csrf
                  middleware/wrap-formats
                  middleware/wrap-nocache
                  ]}
    ["/" {:get home-page}]
    ["/message" {:post save-message!}]
    ["/about" {:get about-page}]
    ]
   ;]
  )

