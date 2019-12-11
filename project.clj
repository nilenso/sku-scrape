(defproject sku-scrape "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [mount "0.1.16"]
                 [aero "1.1.3"]
                 [com.taoensso/carmine "2.19.1"]
                 [compojure "1.6.1"]
                 [enlive "1.1.6"]
                 [crouton "0.1.2"]
                 [ring "1.7.1"]
                 [clj-http "3.10.0"]
                 [ring/ring-json "0.5.0"]]
                                        ; :plugins [[lein-ring "0.12.5"]]
                                        ;  :ring {:handler sku-scrape.server/ring-dev-handler}
  :main ^:skip-aot sku-scrape.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
