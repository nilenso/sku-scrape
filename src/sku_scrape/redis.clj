(ns sku-scrape.redis
  (:require [mount.core :refer [defstate]]
            [sku-scrape.config :refer [config]]))

(defn- connect-redis []
  (prn "connecting redis"))

(defn- disconnect-redis []
  (prn "disconnecting redis"))

(defstate redis-conn
  :start (connect-redis)
  :stop (disconnect-redis))

(def server1-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))
