(ns questionable-scraper.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::name (complement empty?))
(s/def ::price pos-int?)
(s/def ::url (complement empty?))
(s/def ::sku (s/keys :req-un [::name ::price ::url]))
(s/def ::skus (s/coll-of ::sku))
