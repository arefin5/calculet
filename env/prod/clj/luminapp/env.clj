(ns luminapp.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[luminapp started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[luminapp has shut down successfully]=-"))
   :middleware identity})
