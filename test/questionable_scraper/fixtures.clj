(ns questionable-scraper.fixtures
  (:require [mount.core :as mount]
            [questionable-scraper.core :as core]
            [questionable-scraper.scraper :as scraper]
            [questionable-scraper.config :as config]))

(defn setup-once
  [test]
  (config/init! :test)
  (test))

(defn init-state
  [test]
  (mount/start)
  (test)
  (mount/stop))
