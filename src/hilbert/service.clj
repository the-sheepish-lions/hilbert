(ns hilbert.service
  (:use [hiccup.core])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defroutes service
  (GET "/" [] (html [:b "Hello"]))
  (route/not-found (haml [:h1 "Page not found"])))
