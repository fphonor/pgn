(defproject page_generator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.5.1"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "3.9.0"]
                 [environ "1.1.0"]
                 [uncomplicate/fluokitten "0.6.1"]
                 [danlentz/clj-uuid "0.1.7"]
                 [org.clojure/algo.monads "0.1.6"]
                 [enlive "1.1.6"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler page-generator.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
