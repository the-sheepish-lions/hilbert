(ns hilbert.service
  (:use [hiccup.core]
        [hilbert.compiler]
        [hilbert.data.service])
  (:require [clojure.tools.reader.edn :as edn]
            [clojure.string :as s]
            [cognitect.transit :as transit]
            [compojure.core :refer :all]
            [ring.adapter.jetty :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(load "controls/table")

(defn form-file [form]
  (edn/read-string (slurp (str "forms/" form "_fmb.edn"))))

(defn transit-data [data]
  (let [out (java.io.ByteArrayOutputStream. 4096)
        w (transit/writer out :json-verbose)]
    (transit/write w data)
    (.toString out)))

(defn read-transit [data]
  (let [in (java.io.StringBufferInputStream. data)
        r  (transit/reader in :json)]
    (transit/read r)))

(defn nil-or-empty?
  [x]
  (or (nil? x) (empty? x)))

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
     body]
    [:script {:type "text/javascript" :src "/js/main.js"}]]])

(defroutes service
  ;; forms service
  (GET "/form/:form" [form :as {params :params}]
       (html (layout (compile-form (form-file form) params))))

  ;; template service
  (GET "/template/:form" [form]
       (transit-data (compile-form (form-file form))))

  ;; data service
  ;; TODO: add better error checking

  ; projection
  (GET "/data/:source" [source :as {params :params}]
       (let [fields (if (nil-or-empty? (params :fields))
                      []
                      (vec (map keyword (s/split (params :fields)  #"\,"))))
             proj   (projection (keyword source) fields params)]
         (transit-data proj)))
  ; insert
  (POST "/data/:source" [source :as req]
        (let [body (read-transit (slurp (req :body)))
              data (body :values)
              res  (insert (keyword source) data)]
          (transit-data {:insert source :values data})))
  ; update
  (PUT "/data/:source" [source :as req]
        (let [body  (read-transit (slurp (req :body)))
              data  (body :set)
              preds (body :where)
              res   (update (keyword source) data preds)]
          (transit-data {:update source :set data :where preds})))
  ; delete
  (DELETE "/data/:source" [source :as req]
        (let [body  (read-transit (slurp (req :body)))
              preds (body :where)
              res   (delete (keyword source) preds)]
          (transit-data {:delete source :where preds})))

  (route/resources "/")
  (route/not-found (html [:h1 "Page not found"])))

(defn -main [& args]
  (run-jetty (handler/site service) {:port 3000}))
