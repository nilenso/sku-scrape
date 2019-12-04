(ns sku-scrape.threads)

(defn dispatch-scraper [company query]
  (let [time (* (+ (rand-int 5) 1) 1000)]
    (Thread/sleep time)
    (prn (str "scraped skus from  " company " in " time " ms"))
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

(defn api-endpoint [untreated-query]
  (let [response-promise (get-skus-memoized
                          (-> untreated-query
                              clojure.string/lower-case
                              clojure.string/trim))]
    (prn "promise id" response-promise)
    (prn "finally delivered"  @response-promise)))

;;where to add this(shutdown-agents)
