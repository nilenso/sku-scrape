(ns questionable-scraper.handlers-test
  (:require [clojure.test :refer :all]
            [questionable-scraper.handlers :as handlers]))

(deftest scrape-tests
  (testing "Should return 200 with price when called with an sku"
    (let [req {:parameters {:query {:sku "Washing machine"}}}
          res (handlers/scrape req)]
      (is (= 200 (:status res)))))
  (testing "Should return 400 on invalid request"
    (let [req {:parameters {:query {}}}
          res (handlers/scrape req)]
      (is (= 400 (:status res))))))
