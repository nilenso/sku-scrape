(ns sku-scrape.server
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.params :refer [wrap-params]]
            [mount.core :refer [defstate]]
            [ring.util.response :as res]
            [sku-scrape.api :refer [scrape-for-query]]
            [sku-scrape.config :refer [config]]))

(defroutes routes
  (GET "/scrape/:query" [query]
       (scrape-for-query query)))

(defn handler []
  (-> routes
      wrap-json-response
      wrap-params
      wrap-json-body))

(defn- start-server [{:keys [server-spec]}]
  (run-jetty (handler) {:port  (Integer/parseInt (:port server-spec))
                        :host  (:ip server-spec)
                        :join? false}))

(defstate server
  :start (start-server config)
  :stop  (.stop server))
