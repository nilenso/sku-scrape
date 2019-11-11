(ns questionable-scraper.scrapers.amazon
  (:require [clojure.string :as str]
            [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [crouton.html :as crouton]))

(def root-url "https://amazon.in")
(def search-url "https://amazon.in/s?k=%s")
(def sku-selector [:div.a-section :div.sg-row :div.sg-col-4-of-12.sg-col-8-of-16.sg-col-16-of-24.sg-col-12-of-20.sg-col-24-of-32.sg-col-28-of-36.sg-col-20-of-28 :> :div.sg-col-inner])
(def sku-name-selector [:.a-size-mini :.a-link-normal.a-text-normal :span.a-size-medium.a-color-base.a-text-normal])
(def sku-url-selector [:.a-size-mini :.a-link-normal.a-text-normal])
(def sku-price-selector [:.a-size-base :.a-size-base.a-link-normal.a-text-normal :span.a-price-whole])

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
  (str root-url (first (html/attr-values (first (html/select sku sku-url-selector)) :href))))

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
