(ns sku-scrape.core
  (:require
   [sku-scrape.server :refer [server]]
   [mount.core :as mount])
  (:gen-class))

(defn -main
  [& args]
  (mount/start))
