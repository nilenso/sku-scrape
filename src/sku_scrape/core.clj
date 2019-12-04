(ns sku-scrape.core
  (:require
   [sku-scrape.redis  :refer [redis-conn]]
   [mount.core :as mount])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (mount/start)
  (println "Hello, World!"))
