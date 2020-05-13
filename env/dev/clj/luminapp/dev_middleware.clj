(ns luminapp.dev-middleware
  (:require
    [ring.middleware.reload :refer [wrap-reload]]
    [selmer.middleware :refer [wrap-error-page]]
    [prone.middleware :refer [wrap-exceptions]]
    [clojure.tools.logging :as log]))


(defn- pretty-print [d]
  (with-out-str (clojure.pprint/pprint d)))


;(defn add-to-session [request response]
;  (if (get-in request [:session :userid])
;      response
;      (assoc-in response [:session :userid] "hayzee")))
;
;
;(defn- wrap-logging [handler]
;  (fn [request]
;    (log/info "\n\n\nREQUEST DATA " (pretty-print request) "\n\n\n")
;    (let [response (handler request)
;          mod-response (add-to-session request response)]
;      (log/info "\n\n\nRESPONSE DATA " mod-response "\n\n\n")
;      mod-response)))
;(mount.core/defstate reqres-logger :start (fn [name] (str "Hi " name ", i am started")))


(defn- wrap-logging [handler]
  (fn [request]
    (log/info "\n\n\nREQUEST DATA " (pretty-print request) "\n\n\n")
    (let [response (handler request)]
      (log/info "\n\n\nRESPONSE DATA " response "\n\n\n")
      response)))


(defn wrap-dev [handler]
  (-> handler
      wrap-logging
      wrap-reload
      wrap-error-page
      (wrap-exceptions {:app-namespaces ['luminapp]})))

