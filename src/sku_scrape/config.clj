(ns sku-scrape.config
  (:require [mount.core :refer [defstate]]
            [aero.core :as aero]))

(defn read-config [env]
  (aero/read-config
   (clojure.java.io/resource "config/config.edn")
   {:profile env}))

(defstate config
  :start (read-config :dev))
