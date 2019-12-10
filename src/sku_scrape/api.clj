(ns sku-scrape.api
  (:require [ring.util.response :as res]))

(defn dispatch-scraper
  "to be written still."
  [company query]
  (let [time-to-sleep (* (+ (rand-int 20) 1) 500)]
    (Thread/sleep time-to-sleep)
    (prn (str "scraped skus from  " company " in " time-to-sleep " ms"))
    [(str "1-" company) (str "2-" company)]))

(defn combine-skus [skus-from-all-vendors]
  (flatten skus-from-all-vendors))

(defn get-skus [query]
  (let [response (promise)]
    (future
      (dosync
       (deliver
        response
        (combine-skus (pmap #(dispatch-scraper % query) [:flipkart :amazon :myntra :shopclues])))))
    response))

(def get-skus-memoized (memoize get-skus))

(defmacro time-and-return-value [expr]
  `(let [s# (new java.io.StringWriter)]
     {:expr-val (binding [*out* s#]
                  (time ~expr))
      :time-val s#}))

(defn scrape-for-query [untreated-query]
  (let [query            (-> untreated-query
                             clojure.string/lower-case
                             clojure.string/trim)
        response-promise (get-skus-memoized query)]
    (res/response  (time @response-promise))))
