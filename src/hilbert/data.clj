(ns hilbert.data
  (:use [clojure.java.jdbc])
  (:require [clojure.tools.reader.edn :as edn]
            [honeysql.core :as sql]))

(def db (:development (edn/read-string (slurp "resources/database.edn"))))

(defn projection
  [table fields params]
  (let [{:keys [page page-size]} params
        sql (sql/format
              {:select fields
               :from [table]})]
               ;:offset (* page page-size)
               ;:limit page-size})]
    (query db sql)))
