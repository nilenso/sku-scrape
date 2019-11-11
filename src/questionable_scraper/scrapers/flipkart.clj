(ns questionable-scraper.scrapers.flipkart
  (:require [clojure.string :as str]
            [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [crouton.html :as crouton]))

(def root-url "http://www.flipkart.com")
(def search-url "http://www.flipkart.com/search?q=%s")
(def sku-selector [:div.col-12-12 :> :div :> :div :> :div :> :a])
(def sku-name-selector [:div.row :> :div.col-7-12 :> :div])
(def sku-price-selector [:div.row :> :div.col-5-12 :> :div :> :div :> html/first-child])

(defn format-search-url
  [search-keywords]
  (format search-url (java.net.URLEncoder/encode search-keywords)))

(defn fetch-search-results
  [url]
  (crouton/parse-string (:body @(http/get url))))

(defn extract-skus
  [parsed-html]
  (html/select parsed-html sku-selector))

(defn extract-name
  [sku]
  (html/text (first (html/select sku sku-name-selector))))

(defn extract-price
  [sku]
  (html/text (first (html/select sku sku-price-selector))))

(defn extract-url
  [sku]
  (str root-url (first (html/attr-values sku :href))))

(defn extract-details
  [sku]
  {:name  (extract-name sku)
   :price (extract-price sku)
   :url   (extract-url sku)})

(defn fetch-skus
  [search-keywords]
  (->> search-keywords
       format-search-url
       fetch-search-results
       extract-skus
       (map extract-details)))

(comment (fetch-skus "Washing machine"))
