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


;;(defn home-page [request]
;;  (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))
;;    (layout/render request "home.html" {:info {:name "peter"}}))
;; (layout/render request "home.html" {:messages (db/get-messages)}))
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

;
;
;(defn logout [request]
;  (response/found "/login"))
;
;

(defn authenticate [{:keys [params]}]
  (if (= (:password params) "piper")
    (assoc-in
      (response/found "/")
      [:session :authenticated?] true)
    (response/found "/login")))


(defn register [{:keys [params]}]
  ;(if (= (:password params) "piper")
  ;(assoc-in
  ;  (response/found "/")
  ;  [:session :authenticated?] true)
  (response/found "/login"))
;)


;; added by me
;(defn login-page
;  [login-details]
;  {:status  (:status login-details)
;   :headers {"Content-Type" "text/html; charset=utf-8"}
;   :body    (parser/render-file "login.html" login-details)})

(defn home-routes []
  [
   [""
    {:middleware [;middleware/wrap-csrf
                  ;middleware/wrap-formats
                  ;middleware/wrap-nocache
                  ]}
    ["/login" {:get  login-page
               :post authenticate}]                         ;; not protected by the wrap-login middleware
    ["/register" {:get  register-page
                  :post register}]                          ;; not protected by the wrap-login middleware
    ]
   [""
    {:middleware [middleware/wrap-login
                  middleware/wrap-csrf
                  middleware/wrap-formats
                  middleware/wrap-nocache
                  ]}
    ["/" {:get home-page}]
    ["/message" {:post save-message!}]
    ["/about" {:get about-page}]
    ;["/authenticate" {:post authenticate}]
    ;["/login" {:get login-page
    ;           :post login-page}]
    ["/logout" {:get login-page}]
    ]
   ]
  )

