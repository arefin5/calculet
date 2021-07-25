(ns luminapp.dev-middleware
  (:require
    [ring.middleware.reload :refer [wrap-reload]]
    [selmer.middleware :refer [wrap-error-page]]
    [prone.middleware :refer [wrap-exceptions]]
    [clojure.tools.logging :as log]))


(defn- pretty-print [d]
  (with-out-str (clojure.pprint/pprint d)))


(def ^:dynamic *log-enabled* true)

(defn log-request [reqres label]
  (when *log-enabled*
    (log/info "\n\n\n" label ":" (pretty-print reqres) "\n\n\n")))


(defn- wrap-logging [handler]
  (fn [request]
    (log-request request "Request")
    (let [response (handler request)]
      (log-request response "Response")
      response)))


(defn wrap-dev [handler]
  (-> handler
      wrap-logging
      wrap-reload
      wrap-error-page
      (wrap-exceptions {:app-namespaces ['luminapp]})))

