(ns page-generator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [net.cgrand.enlive-html :as eh]
            [clj-http.client :as http]
            [taoensso.timbre :as timbre
             :refer [log  trace  debug  info  warn  error  fatal  report
                     logf tracef debugf infof warnf errorf fatalf reportf
                     spy get-env]]
            [taoensso.timbre.appenders.core :as appenders]
            [environ.core :as e]
            [clojure.data.json :as json]
            [clojure.java.io :as io]))

(timbre/merge-config!
    {:appenders {:spit (appenders/spit-appender {:fname "log/pgn.log"})}})

(print (sort (keys e/env)))
(e/env :user-country)

(defn set-attr-args-handler [args data]
  (loop [[attr user-attr & xs] args
         result []]
    (let [res (conj result
                    (keyword attr)
                    (get data user-attr))]
      (if (nil? xs)
        res
        (recur xs res)))))


(def handlers {
               :set-attr [eh/set-attr set-attr-args-handler]
               :html-content
               [eh/html-content
                (fn [[user-attr] data]
                  [(get data user-attr)])]
               })

(defn make-selector-transforms-real
  [data [css-selector [handler-name & handler-args]]]
  (list (vec
          (map keyword
               (-> css-selector 
                   clojure.string/trim
                   (clojure.string/split #" +"))))
        (let [[handler args-converter] ((keyword handler-name) handlers)]
          `(~handler ~@(args-converter handler-args data)))))

(defn link-transform [node]
  (println node)
  (update-in node
             [:attrs :href]
             (fn [old-value prefix]
               (str prefix old-value))
             "http://129.0.2.66/dist_0620/"))

(defmacro get-html-content-template
  [url-str data selector-transforms]
  (let [keyword-selector-transforms (map (partial make-selector-transforms-real data) selector-transforms)]
   `(eh/template
     (io/as-url ~url-str)
     []
     [:head :link] link-transform
     ~@(apply concat keyword-selector-transforms))))


(defn get-proper-html-content
  [url-str data selector-transforms]
  (apply str 
         ((eval
            (read-string
              (str
                "(page-generator.handler/get-html-content-template "
                   (str "\"" url-str "\"")
                   data
                   selector-transforms
                   ")"))))))


(defroutes app-routes
  (GET "/generate" [url user-info selector-transforms data]
       (let [data' (if (nil? data)
                    (json/read-str user-info)
                    (json/read-str data))
             selector-transforms' (json/read-str selector-transforms)]
       (spy :info url)
       (spy :debug data')
       (spy :debug selector-transforms')
       (get-proper-html-content url data' selector-transforms')))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
