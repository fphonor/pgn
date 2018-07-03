(ns page-generator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [net.cgrand.enlive-html :as eh]
            [clj-http.client :as http]
            [environ.core :as e]
            [clojure.data.json :as json]
            [clojure.java.io :as io]))

(print (sort (keys e/env)))
(e/env :user-country)

(defn set-attr-args-handler [args user-info]
  (loop [[attr user-attr & xs] args
         result []]
    (let [res (conj result
                    (keyword attr)
                    (get user-info user-attr))]
      (if (nil? xs)
        res
        (recur xs res)))))


(def handlers {
               :set-attr [eh/set-attr set-attr-args-handler]
               :html-content
               [eh/html-content
                (fn [[user-attr] user-info]
                  [(get user-info user-attr)])]
               })

(defn make-selector-transforms-real
  [user-info [css-selector [handler-name & handler-args]]]
  (list (vec
          (map keyword
               (-> css-selector 
                   clojure.string/trim
                   (clojure.string/split #" +"))))
        (let [[handler args-converter] ((keyword handler-name) handlers)]
          `(~handler ~@(args-converter handler-args user-info)))))

(defn link-transform [node]
  (println node)
  (update-in node
             [:attrs :href]
             (fn [old-value prefix]
               (str prefix old-value))
             "http://129.0.2.66/dist_0620/"))

(defmacro get-html-content-template
  [url-str user-info selector-transforms]
  (let [keyword-selector-transforms (map (partial make-selector-transforms-real user-info) selector-transforms)]
   `(eh/template
     (io/as-url ~url-str)
     []
     [:head :link] link-transform
     ~@(apply concat keyword-selector-transforms))))


(defn get-proper-html-content
  [url-str user-info selector-transforms]
  (apply str 
         ((eval
            (read-string
              (str
                "(page-generator.handler/get-html-content-template "
                   (str "\"" url-str "\"")
                   user-info
                   selector-transforms
                   ")"))))))


(defroutes app-routes
  (GET "/generate" [url user-info selector-transforms]
       (println url)
       (println user-info)
       (println selector-transforms)
       (get-proper-html-content url (json/read-str user-info) (json/read-str selector-transforms)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
