(ns questionable-scraper.scraper-test
  (:require [clojure.test :refer :all]
            [questionable-scraper.scraper :as scraper]
            [questionable-scraper.fixtures :refer [init-state setup-once]]))

(use-fixtures :once setup-once)
(use-fixtures :each init-state)

(def sample-sku "Washing Machine")

(deftest scrape-tests
  (testing "Task should run only once for n concurrent calls with same sku"
    (let [call-count 100
          counter    (atom 0)]
      (with-redefs [scraper/run-scrapers (fn [sku p]
                                           (swap! counter inc)
                                           (Thread/sleep 300)
                                           (deliver p (str "Price of" sku)))]
        (doall (pmap scraper/scrape (repeat 100 sample-sku)))
        ;; Check if count of pmap is = call-count
        (is (= 1 @counter))))))
