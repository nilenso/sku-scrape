(ns questionable-scraper.scraper
  (:require [mount.core :refer [defstate]]
            [questionable-scraper.scrapers.flipkart :as flipkart]
            [questionable-scraper.scrapers.amazon :as amazon]))

(def prices (ref {}))
(defstate initial-prices :start (dosync (ref-set prices {})))
(def scrapers [flipkart/fetch-skus amazon/fetch-skus])
(def no-of-results 20)

(defn run-task
  [sku p]
  (let [search-results (->> (doall (pmap #(% sku) scrapers))
                            flatten
                            (sort-by :price)
                            (take no-of-results))]
    ;; TODO: Insert first result in DB
    (deliver p search-results)))

(defn scrape
  [sku]
  (dosync
   (when-not (get (ensure prices) sku)
     (let [p (promise)]
       (alter prices assoc sku p)
       (run-task sku p))))
  @(get @prices sku))

(comment (scrape "Panasonic Washing machine"))
