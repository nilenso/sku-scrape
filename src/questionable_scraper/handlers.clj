(ns questionable-scraper.handlers
  (:require [questionable-scraper.scraper :as scraper]
            [questionable-scraper.views.results :as results]))

(defn scrape [req]
  (if-let [sku (get-in req [:parameters :query :sku])]
    {:status 200
     :body   (results/results-page req (scraper/scrape sku))}
    {:status 400 :body "Invalid request."}))
