(ns sku-scrape.scraper
  (:require [clj-http.client :as http]
            [crouton.html :as html]
            [net.cgrand.enlive-html :as enlive]))

(def platforms {:amazon
                {:query-link            "https://www.amazon.in/s?k="
                 :results-selector      ;[:div.a-section :div.sg-row :div.sg-col-4-of-12.sg-col-8-of-16.sg-col-16-of-24.sg-col-12-of-20.sg-col-24-of-32.sg-col-28-of-36.sg-col-20-of-28 :> :div.sg-col-inner]
                 [:.sg-col-4-of-12.sg-col-8-of-16.sg-col-16-of-24.sg-col-12-of-20.sg-col-24-of-32.sg-col.sg-col-28-of-36.sg-col-20-of-28]
                 :result-deconstruction {:price     {:selector    [:.a-price-whole]
                                                     :constructor [:content 0]}
                                         :currency  {:selector    [:.a-price-symbol]
                                                     :constructor [:content 0]}
                                         :link      {:selector    [:h2 (enlive/attr? :href)]
                                                     :constructor [:attrs :href]}
                                         :link-text {:selector    [:h2 (enlive/attr? :href)]
                                                     :constructor [:content 1 :content 0]}}}
                :flipkart
                {:query-link            "https://www.flipkart.com/search?q="
                 :results-selector      [:.col-12-12 :> :div :> :div]
                 :result-deconstruction []}
                :shopclues
                {:query-link            "https://www.shopclues.com/search?q="
                 :results-selector      []
                 :result-deconstruction []}
                :myntra
                {:query-link            "https://www.myntra.com/"
                 :results-selector      []
                 :result-deconstruction []}})


(defn get-query-result-dom
  [query platform]
  (let [query-url (str (get-in platforms [platform :query-link]) query)]
    (html/parse-string (:body (http/get query-url)))))

(defn sku-list-extractor
  [data platform]
  (enlive/select data (get-in platforms [platform :results-selector])))

(defn sku-param-extractor
  [data param-extraction-format]
  (get-in
   (first (enlive/select data (:selector param-extraction-format)))
   (:constructor param-extraction-format)))

(defn sku-data-extractor [single-sku-data sku-deconstruction-format]
  (reduce
   (fn [acc k]
     (conj acc {k (sku-param-extractor single-sku-data (k sku-deconstruction-format))}))
   {}
   (keys sku-deconstruction-format)))

(defn sku-extractor [sku-list-expanded platform]
  (map
   #(sku-data-extractor % (get-in platforms [platform :result-deconstruction]))
   sku-list-expanded))

(defn get-sku-results
  [search-query platform]
  (-> search-query
      (get-query-result-dom platform)
      (sku-list-extractor platform)
      (sku-extractor platform)))

#_(defn sku-test
    [query platform]
    (-> query
        (get-query-result-dom platform)
        (sku-list-extractor platform)))
