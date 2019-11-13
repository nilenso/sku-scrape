(ns questionable-scraper.routes
  (:require [muuntaja.core :as m]
            [questionable-scraper.handlers :as handlers]
            [questionable-scraper.views.index :as index]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]))

(def app-routes
  (ring/router
   [["/" {:get {:handler (fn [req]
                           {:body (index/index-page req)})}}]
    ["/scrape"
     {:get {:parameters {:query {:sku string?}}
            :handler    handlers/scrape}}]]
   {:data {:coercion   reitit.coercion.spec/coercion
           :muuntaja   m/instance
           :middleware [parameters/parameters-middleware
                        muuntaja/format-negotiate-middleware
                        muuntaja/format-response-middleware
                        muuntaja/format-request-middleware
                        coercion/coerce-exceptions-middleware
                        coercion/coerce-response-middleware
                        coercion/coerce-request-middleware]}}))
