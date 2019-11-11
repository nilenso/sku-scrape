(ns questionable-scraper.utils)

(defn string->int
  [string]
  (let [sanitized-string (.replaceAll string "[^0-9]" "")]
    (try
      (Integer/parseInt sanitized-string)
      (catch Exception e sanitized-string))))
