(ns questionable-scraper.scrapers.amazon-test
  (:require [clojure.test :refer :all]
            [questionable-scraper.scrapers.amazon :as amazon]
            [net.cgrand.enlive-html :as html]
            [clojure.spec.alpha :as s]))

(def sample-sku "Washing machine")
(s/def ::name (complement empty?))
(s/def ::price pos-int?)
(s/def ::url (complement empty?))
(s/def ::sku (s/keys :req-un [::name ::price ::url]))
(s/def ::skus (s/coll-of ::sku))

(deftest fetch-test
  (let [page-html (amazon/fetch-search-results (amazon/format-search-url sample-sku))]
    (testing "Tests if fetch search results returns valid amzon html page."
      (is (some? (:content (first (html/select page-html [:#a-page]))))))

    (testing "Tests if extract-details gets right data."
      (is (s/valid? ::skus (->> (amazon/extract-skus page-html)
                                (map amazon/extract-details)))))))
