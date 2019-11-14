(ns questionable-scraper.scraper
  (:require [mount.core :refer [defstate]]
            [questionable-scraper.scrapers.flipkart :as flipkart]
            [questionable-scraper.scrapers.amazon :as amazon]
            [questionable-scraper.scrapers.shopclues :as shopclues]))

(def prices (ref {}))
(defstate initial-prices :start (dosync (ref-set prices {})))
(def scrapers [flipkart/fetch-skus amazon/fetch-skus shopclues/fetch-skus])
(def no-of-results 100)

(defn run-scrapers
  [sku p]
  (deliver p (->> (doall (pmap #(% sku) scrapers))
                  flatten
                  (sort-by :price)
                  (take no-of-results))))

(defn scrape
  [sku]
  (dosync
   (when-not (get (ensure prices) sku)
     (let [p (promise)]
       (alter prices assoc sku p)
       (run-scrapers sku p))))
  @(get @prices sku))

(comment (scrape "Panasonic Washing machine"))
