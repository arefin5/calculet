(ns luminapp.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [luminapp.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[luminapp started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[luminapp has shut down successfully]=-"))
   :middleware wrap-dev})
