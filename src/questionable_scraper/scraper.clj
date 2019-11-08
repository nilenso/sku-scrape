(ns questionable-scraper.scraper
  (:require [mount.core :refer [defstate]]))

(def prices (ref {}))
(defstate initial-prices :start (dosync (ref-set prices {})))

(defn run-task [p]
  (println "Calling API")
  (Thread/sleep 3000)
  (deliver p "Price is 100 Rs."))

(defn scrape
  [sku]
  (dosync
   (if-not (get (ensure prices) sku)
     (let [p (promise)]
       (alter prices assoc sku p)
       (run-task p))))
  @(get @prices sku))
