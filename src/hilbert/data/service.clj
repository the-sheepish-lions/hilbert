(ns hilbert.data.service
  (:use [clojure.java.jdbc] [hilbert.util])
  (:require [clojure.tools.reader.edn :as edn]
            [honeysql.core :as sql]))

(def db (:development (edn/read-string (slurp "resources/database.edn"))))

;; pagination
; SELECT
;   FWBAGNT_ORGN_CODE,
;   FWBAGNT_CONTROL_AGENT1,
;   FWBAGNT_CONTROL_AGENT2,
;   FWBAGNT_CONTROL_AGENT3,
;   FWBAGNT_USER_ID,
;   FWBAGNT_ACTIVITY_DATE,
;   FWBAGNT_VERSION,
;   FWBAGNT_DATA_ORIGIN
; FROM (SELECT a.*, ROWNUM rnum
;       FROM (SELECT * FROM FWBAGNT ORDER BY FWBAGNT_ORGN_CODE) a WHERE ROWNUM <=  end)
; WHERE rnum >= start;

(defn- start [page page-size]
  (if (= 1 page)
    1
    (inc (* (dec page) page-size))))

(defn- end [page page-size]
  (dec (+ (start page page-size) page-size)))

(defn projection
  [table fields params]
  (prn :projection-pre params)
  (let [p     (string->int (get params :page 1))
        psize (string->int (get params :page-size 20))
        order (string->keyword (get params :sort-by))
        smap  {:select fields
               :from [{:select [:a.* [:ROWNUM :rnum]]
                       :from [[{:select [:*]
                                :from [table]
                                :order-by [order]} :a]]
                       :where [:<= :ROWNUM (sql/param :end)]}]
               :where [:>= :rnum (sql/param :start)]}
        sql   (sql/format smap {:start (start p psize) :end (end p psize)})
        csql  (sql/format {:select [:%count.*] :from [table]})]
    (prn :projection-post params smap sql)
    {:table  table
     :fields fields
     :params params
     :count  (int ((first (query db csql)) (keyword "count(*)")))
     :data   (query db sql)}))

(defn- eval-pred-map
  [preds]
  (cons :and (map #(vector := (%1 0) (%1 1)) preds)))

(defn insert
  [table values]
  (let [smap {:insert-into table :values values}
        sql  (sql/format smap)]
    (prn :insert smap sql)
    (execute! db sql)))

(defn update
  [table data & preds]
  (let [smap  {:update table :set data}
        smap* (if-let [p (first preds)] (assoc smap :where (eval-pred-map p)) smap)
        sql   (sql/format smap*)]
    (prn :update smap* sql)
    (execute! db sql)))

(defn delete
  [table preds]
  (let [smap {:delete-from table :where (eval-pred-map preds)}
        sql  (sql/format smap)]
    (prn :delete smap sql)
    (execute! db sql)))
