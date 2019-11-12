(ns questionable-scraper.views.index
  (:require [hiccup.core :refer [html]]))

(defn index-page
  [req]
  (html
   [:body
    [:h3 "The questionable scraper"]
    [:form {:action "/scrape" :method "GET"}
     [:input {:type "text" :name "sku" :placeholder "Enter search keywords"}]
     [:br]
     [:br]
     [:button {:type "submit"} "Submit"]]]))
