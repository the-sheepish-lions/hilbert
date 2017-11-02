(ns hilbert.data.service
  (:use [clojure.java.jdbc])
  (:require [clojure.tools.reader.edn :as edn]
            [honeysql.core :as sql]))

(def db (:development (edn/read-string (slurp "resources/database.edn"))))

(defn projection
  [table fields params]
  (let [{:keys [page page-size order-by]} params
        smap {:select fields :from [table]}
        smap* (if order-by (assoc smap :order-by [order-by]) smap)
        sql (sql/format smap*)]
               ;:offset (* page page-size)
               ;:limit page-size})]
    (prn smap* sql)
    (query db sql)))
