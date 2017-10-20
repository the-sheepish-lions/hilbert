(ns hilbert.service
  (:use [hiccup.core]
        [hilbert.compiler])
  (:require [clojure.tools.reader.edn :as edn]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn form-file [form]
  (edn/read-string (slurp (str "forms/" form "_fmb.edn"))))

(defn layout [body]
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
  (GET "/form/:form" [form] (html (layout (compile-form (form-file form)))))
  (route/not-found (html [:h1 "Page not found"])))
