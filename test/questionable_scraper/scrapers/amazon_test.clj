(ns questionable-scraper.scrapers.amazon-test
  (:require [clojure.test :refer :all]
            [net.cgrand.enlive-html :as html]
            [clojure.spec.alpha :as s]
            [questionable-scraper.spec :as spec]
            [questionable-scraper.scrapers.amazon :as amazon]))

(def sample-sku "Washing machine")

(deftest fetch-test
  (let [page-html (amazon/fetch-search-results (amazon/format-search-url sample-sku))]
    (testing "Tests if fetch search results returns valid amzon html page."
      (is (some? (:content (first (html/select page-html [:#a-page]))))))

    (testing "Tests if extract-details gets right data."
      (is (s/valid? ::spec/skus (->> (amazon/extract-skus page-html)
                                     (map amazon/extract-details)
                                     amazon/remove-invalid-skus))))))
