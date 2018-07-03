(ns page-generator.handler-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [net.cgrand.enlive-html :as eh]
            [clojure.java.io :as io]
            [ring.mock.request :as mock]
            [page-generator.handler :refer :all])
  (:import [java.net URLEncoder]))
  (URLEncoder/encode "http://foo bar/" "UTF-8")

;; (def uinf
;;   {"city" ""
;;    "country" "中国"
;;    "groupid" 0
;;    "headimgurl" "http//thirdwx.qlogo.cn/mmopen/ajNVdqHZLLDA3eHR4Nvia467fOBxgW26ofcEagcvCencDJHicscvRnKk8XQqRXAKvGkw67J9uWjA3Q6gIUY8vlqQ/132"
;;    "language" "zh_CN"
;;    "nickname" "依古比古丶"
;;    "openid" "oHlzFs2kXZ-gYYlExPiSljyhlHH8"
;;    "province" ""
;;    "qr_scene" 0
;;    "qr_scene_str" ""
;;    "remark" ""
;;    "sex" 1
;;    "subscribe" 1
;;    "subscribe_scene" "ADD_SCENE_QR_CODE"
;;    "subscribe_time" 1510066409
;;    "tagid_list" []
;;    "unionid" "opvTbsoyqT77NmBKeMroM6SKOZQo"})
;; 
;; (def sf
;;   {"body .name" ["html-content" "nickname"]
;;    ".avatar img" ["set-attr" "src" "headimgurl"]
;;    ".code img" ["set-attr" "src" "headimgurl"]
;;    })
;; 
;; (=
;;  (URLEncoder/encode 
;;    (json/write-str sf)
;;    "UTF-8")
;;   "%7B%22body+.name%22%3A%5B%22html-content%22%2C%22nickname%22%5D%2C%22.avatar+img%22%3A%5B%22set-attr%22%2C%22src%22%2C%22headimgurl%22%5D%2C%22.code+img%22%3A%5B%22set-attr%22%2C%22src%22%2C%22headimgurl%22%5D%7D")
;; 
;; (=
;;  (count (URLEncoder/encode
;;    (URLEncoder/encode 
;;      (json/write-str sf)
;;      "UTF-8")))
;;   (count "%257B%2522body%2520.name%2522%253A%255B%2522html-content%2522%252C%2522nickname%2522%255D%252C%2522.avatar%2520img%2522%253A%255B%2522set-attr%2522%252C%2522src%2522%252C%2522headimgurl%2522%255D%252C%2522.code%2520img%2522%253A%255B%2522set-attr%2522%252C%2522src%2522%252C%2522headimgurl%2522%255D%257D"))
;; (get-proper-html-content 
;;   "file:///Users/pyskell/Downloads/dist/skku.html"
;;   uinf sf)
;; 
(print (apply str ((eh/template
 ;; (io/as-url "file:///Users/pyskell/Downloads/dist/skku.html")
 (io/resource "dist_0620_new/skku.html")
   []
   [:head :link] (fn [node]
                   (println node)
                   (update-in node
                              [:attrs :href]
                              (fn [old-value prefix]
                                (str prefix old-value))
                              "http://54.223.139.77/dist_0620/"))))))
;; 
;; (deftest test-app
;;   (testing "main route"
;;     (let [response (app (mock/request :get "/"))]
;;       (is (= (:status response) 200))
;;       (is (= (:body response) "Hello World"))))
;; 
;;   (testing "not-found route"
;;     (let [response (app (mock/request :get "/invalid"))]
;;       (is (= (:status response) 404)))))
