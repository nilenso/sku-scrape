(ns questionable-scraper.config
  (:require [aero.core :refer (read-config)]
            [clojure.java.io :as io]))

(defonce config (atom nil))

(defn init!
  ([]
   (init! :default))
  ([profile]
   (reset! config (read-config (io/resource "config.edn") {:profile profile}))))

(defn http-port
  []
  (:http-port @config))
