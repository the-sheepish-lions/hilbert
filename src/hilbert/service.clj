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

;; TODO: running service with site handler obviates this
(defn query-params [s]
  (if (nil? s)
    {}
    (into {} (map #(vec (s/split % #"\=")) (s/split s #"\&")))))

(defn form-params [params]
  (let [params* (query-params (params :query-string))
        p       (get params* "p")
        psize   (get params* "psize")
        order   (get params* "sort-by")
        proto   (atom {})]
    (if-not (or (nil? p) (empty? p))
      (swap! proto assoc :page (Integer/parseInt p)))
    (if-not (or (nil? psize) (empty? psize))
      (swap! proto assoc proto :page-size (Integer/parseInt psize)))
    (if-not (or (nil? order) (empty? order))
      (swap! proto assoc proto :sort-by order))
    @proto))

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
  (GET "/form/:form" [form :as params]
       (html (layout (compile-form (form-file form) (form-params params)))))

  ;; template service
  (GET "/template/:form" [form]
       (transit-data (compile-form (form-file form))))

  ;; data service
  ;; TODO: added more error checking
  ; projection
  (GET "/data/:source" [source :as {q :query-string}]
       (let [params (query-params q)
             fields (vec (map keyword (s/split (params "fields") #"\,")))
             pro (projection (keyword source) fields {})]
         (transit-data pro)))
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
  ;(println "Hello"))
  (run-jetty (handler/site service) {:port 3000}))
