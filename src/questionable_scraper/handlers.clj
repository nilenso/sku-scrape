(ns questionable-scraper.handlers
  (:require [questionable-scraper.scraper :as scraper]))

(defn scrape [req]
  (if-let [sku (get-in req [:parameters :query :sku])]
    {:status 200
     :body   (scraper/scrape sku)}
    {:status 400 :body "Invalid request."}))
