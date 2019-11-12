(defproject questionable-scraper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [http-kit "2.4.0-alpha3"]
                 [metosin/reitit "0.3.10"]
                 [mount "0.1.16"]
                 [aero "1.1.3"]
                 [enlive "1.1.6"]
                 [crouton "0.1.2"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-ring "0.12.5"]
            [lein-cloverage "1.1.2"]]
  :main questionable-scraper.core
  :aot [questionable-scraper.core]
  :ring {:handler questionable-scraper.core/app-handler}
  :repl-options {:init-ns questionable-scraper.core})
