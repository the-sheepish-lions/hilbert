(ns hilbert.service
  (:use [hiccup.core]
        [hilbert.compiler]
        [hilbert.data])
  (:require [clojure.tools.reader.edn :as edn]
            [clojure.string :as s]
            [cognitect.transit :as transit]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn form-file [form]
  (edn/read-string (slurp (str "forms/" form "_fmb.edn"))))

(defn transit-data [data]
  (let [out (java.io.ByteArrayOutputStream. 4096)
        w (transit/writer out :json-verbose)]
    (transit/write w data)
    (.toString out)))

(defn query-params [s]
  (into {} (map #(vec (s/split % #"\=")) (s/split s #"\&"))))

(defn layout
  [body]
  [:html
   [:head
    [:link {:rel "stylesheet"
            :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
            :integrity "sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M"
            :crossorigin "anonymous"}]
    [:title "CNM Forms"]]
   [:body
    [:div {:class "container" :id "main"}
     body]]])

(defroutes service
  (GET "/form/:form" [form]
       (html (layout (compile-form (form-file form)))))
  (GET "/template/:form" [form]
       (transit-data (compile-form (form-file form))))
  (GET "/data/:source" [source :as {q :query-string}]
       (let [params (query-params q)
             fields (vec (map keyword (s/split (params "fields") #"\,")))
             pro (projection (keyword source) fields {})]
         (transit-data pro)))
  (route/not-found (html [:h1 "Page not found"])))
