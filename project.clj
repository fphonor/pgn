(defproject page_generator "0.1.0-SNAPSHOT"
  :description "web page html generator: use the `data` and the `selector-transforms`"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 ;; For logging
                 [ring-logger "1.0.1"]
                 [com.taoensso/timbre "4.10.0"]

                 ;; For JSON
                 [org.clojure/data.json "0.2.6"]

                 ;; For http request
                 [clj-http "3.9.0"]

                 ;; For enviroment variables
                 [environ "1.1.0"]

                 ;; For monad
                 [uncomplicate/fluokitten "0.6.1"]
                 [org.clojure/algo.monads "0.1.6"]

                 ;; For uuid
                 [danlentz/clj-uuid "0.1.7"]

                 ;; For html content generate
                 [enlive "1.1.6"]]
  :plugins [[lein-ring "0.12.4"]]
  :ring {:handler page-generator.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
