(ns questionable-scraper.core
  (:require [muuntaja.core :as m]
            [questionable-scraper.handlers :as handlers]
            [questionable-scraper.config :as config]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [org.httpkit.server :as httpkit]))

(defonce server (atom nil))

(def app-handler
  (ring/ring-handler
    (ring/router
     [["/" {:get {:handler (fn [req]
                             {:body "<a href=\"/scrape?sku=Washing machine\">Scrape</a>"})}}]
      ["/scrape"
       {:get {:parameters {:query {:sku string?}}
              :handler    handlers/scrape}}]]
     {:data {:coercion   reitit.coercion.spec/coercion
             :muuntaja   m/instance
             :middleware [parameters/parameters-middleware
                          muuntaja/format-negotiate-middleware
                          muuntaja/format-response-middleware
                          muuntaja/format-request-middleware
                          coercion/coerce-response-middleware
                          coercion/coerce-request-middleware]}})
    (ring/routes
      (ring/create-default-handler))))

(defn start! []
  (reset! server (httpkit/run-server app-handler {:port (config/http-port)}))
  (println "Running on port" (config/http-port)))

(defn stop! []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main
  [& args]
  (config/init!)
  (start!))
