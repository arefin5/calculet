(ns luminapp.middleware
  (:require
    [luminapp.env :refer [defaults]]
    [cheshire.generate :as cheshire]
    [cognitect.transit :as transit]
    [clojure.tools.logging :as log]
    [luminapp.layout :refer [error-page]]
    [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
    [luminapp.middleware.formats :as formats]
    [muuntaja.middleware :refer [wrap-format wrap-params]]
    [luminapp.config :refer [env]]
    [ring-ttl-session.core :refer [ttl-memory-store]]
    [ring.middleware.defaults :refer [secure-site-defaults site-defaults wrap-defaults]]

    [clojure.tools.logging :as log]
))


(defn wrap-login [handler]
  (fn [request]
    (log/info "wrap-login")
    (if (-> request :session :authenticated?)
      (handler request)
      (error-page {:status 401
                   :title "Not logged in"
                   :message (str "session " (:session request))}))))


    ;(-> request
    ;    handler
    ;    ;(update-in [:headers] assoc
    ;    ;           "Cache-Control" "no-cache, no-store, must-revalidate"
    ;    ;           "Pragma" "no-cache"
    ;    ;           "expires" "0")
    ;    )))

;; added by me:
(defn wrap-nocache [handler]
  (fn [request]
    (log/info "wrap-nocache")
    (-> request
        handler
        (update-in [:headers] assoc
                   "Cache-Control" "no-cache, no-store, must-revalidate"
                   "Pragma" "no-cache"
                   "expires" "0"))))


(defn- wrap-internal-error [handler]
  (fn [req]
    (log/info "wrap-internal-error")
    (try
      (handler req)
      (catch Throwable t
        (log/error t (.getMessage t))
        (error-page {:status 500
                     :title "Something very bad has happened!"
                     :message "We've dispatched a team of highly trained gnomes to take care of the problem."})))))


(defn wrap-csrf [handler]
  (wrap-anti-forgery
    handler
    {:error-response
     (error-page
       {:status 403
        :title "Invalid anti-forgery token"})}))


(defn wrap-formats [handler]
  (let [wrapped (-> handler wrap-params (wrap-format formats/instance))]
    (fn [request]
      (log/info "wrap-formats")
      ;; disable wrap-formats for websockets
      ;; since they're not compatible with this middleware
      ((if (:websocket? request) handler wrapped) request))))


(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      (wrap-defaults
        (-> site-defaults
;        (-> secure-site-defaults
            (assoc-in [:security :anti-forgery] false)
            (assoc-in [:session :store] (ttl-memory-store (* 60 30)))
            ))
      wrap-internal-error))

