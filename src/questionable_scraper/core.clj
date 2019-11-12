(ns questionable-scraper.core
  (:gen-class)
  (:require [org.httpkit.server :as httpkit]
            [questionable-scraper.config :as config]
            [questionable-scraper.routes :as routes]
            [reitit.ring :as ring]))

(defonce server (atom nil))

(def app-handler
  (ring/ring-handler
    routes/app-routes
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
