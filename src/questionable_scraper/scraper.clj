(ns questionable-scraper.scraper
  (:require [mount.core :refer [defstate]]
            [questionable-scraper.scrapers.flipkart :as flipkart]
            [questionable-scraper.scrapers.amazon :as amazon]
            [questionable-scraper.scrapers.shopclues :as shopclues]))

(def scrapers [flipkart/fetch-skus amazon/fetch-skus shopclues/fetch-skus])
(def no-of-results 100)

(defn run-scrapers
  ([sku]
   (->> (doall (pmap #(% sku) scrapers))
        flatten
        (sort-by :price)
        (take no-of-results))))

;; Use a ref to cache requests
(def prices (ref {}))
(defstate initial-prices :start (dosync (ref-set prices {})))

(defn scrape
  [sku]
  (dosync
   (when-not (get (ensure prices) sku)
     (alter prices assoc sku (delay
                               (run-scrapers sku)))))
  @(get @prices sku))

(comment (scrape "Panasonic Washing machine"))
