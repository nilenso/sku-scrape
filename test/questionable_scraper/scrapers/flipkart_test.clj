(ns questionable-scraper.scrapers.flipkart-test
  (:require [clojure.test :refer :all]
            [questionable-scraper.scrapers.flipkart :as flipkart]
            [net.cgrand.enlive-html :as html]
            [clojure.spec.alpha :as s]))

(def sample-sku "Washing machine")
(s/def ::name (complement empty?))
(s/def ::price pos-int?)
(s/def ::url (complement empty?))
(s/def ::sku (s/keys :req-un [::name ::price ::url]))
(s/def ::skus (s/coll-of ::sku))

(deftest fetch-test
  (let [page-html (flipkart/fetch-search-results (flipkart/format-search-url sample-sku))]
    (testing "Tests if fetch search results returns valid flipkart html page."
      (is (some? (:content (first (html/select page-html [:#container]))))))

    (testing "Tests if extract-details gets right data."
      (is (s/valid? ::skus (->> (flipkart/extract-skus page-html)
                                (map flipkart/extract-details)))))))
