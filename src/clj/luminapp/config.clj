(ns luminapp.config
  (:require
    [cprop.core :refer [load-config]]
    [cprop.source :as source]
    [mount.core :refer [args defstate]]))

(defstate env
  :start
  (load-config
    :merge
    [(args)
     (source/from-system-props)
     (source/from-env)]))

; Here's a way to check for intersecting keys:
; (filter #(< 1 (second %))
;   (frequencies
;     (concat
;       (keys (source/from-system-props))
;       (keys (source/from-env)))))

