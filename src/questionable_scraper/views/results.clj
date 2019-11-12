(ns questionable-scraper.views.results
  (:require [hiccup.core :refer [html]]))

(defn results-page
  [req sku-list]
  (html
   [:body
    (if-not (empty? sku-list)
      [:ul
       (doall
        (for [sku sku-list]
          [:li
           (str (:vendor sku) " - ")
           [:a {:href (:url sku)} (:name sku)]
           (str " - &#8377; " (:price sku))]))]
      [:div
       (str "No results found for: ")
       [:h3 {:style "margin-top: 5px"} (get-in req [:parameters :query :sku])]
       [:a {:href "/"} "Try another search"]])]))
