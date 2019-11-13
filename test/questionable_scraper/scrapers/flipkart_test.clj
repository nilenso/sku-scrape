(ns questionable-scraper.scrapers.flipkart-test
  (:require [clojure.test :refer :all]
            [net.cgrand.enlive-html :as html]
            [clojure.spec.alpha :as s]
            [questionable-scraper.spec :as spec]
            [questionable-scraper.scrapers.flipkart :as flipkart]))

(def sample-sku "Washing machine")

(deftest fetch-test
  (let [page-html (flipkart/fetch-search-results (flipkart/format-search-url sample-sku))]
    (testing "Tests if fetch search results returns valid flipkart html page."
      (is (some? (:content (first (html/select page-html [:#container]))))))

    (testing "Tests if extract-details gets right data."
      (let [results (->> (flipkart/extract-skus page-html)
                                     (map flipkart/extract-details)
                                     flipkart/remove-invalid-skus)]
        (is (not-empty results))
        (is (s/valid? ::spec/skus results))))))
