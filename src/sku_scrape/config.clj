(ns sku-scrape.config
  (:require [mount.core :refer [defstate]]
            [aero.core :as aero]))

(def ^:private specs (atom nil))

(defn read-config [env]
  (let [config (aero/read-config
                (clojure.java.io/resource "config/config.edn")
                {:profile env})]
    (prn "reading config")
    (reset! specs config)))

(defn reset-config []
  (prn "resetting config")
  (reset! specs nil))

(defstate config
  :start (read-config :dev)
  :stop (reset-config))
