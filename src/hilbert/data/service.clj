(ns hilbert.data.service
  (:use [clojure.java.jdbc])
  (:require [clojure.tools.reader.edn :as edn]
            [honeysql.core :as sql]))

(def db (:development (edn/read-string (slurp "resources/database.edn"))))

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

(defn start [page page-size]
  (if (= 1 page)
    1
    (inc (* (dec page) page-size))))

(defn end [page page-size]
  (dec (+ (start page page-size) page-size)))

(defn projection
  [table fields params]
  (prn :projection-pre params)
  (let [p     (get params :page 1)
        psize (get params :page-size 20)
        order (get params :sort-by)
        smap  {:select fields
               :from [{:select [:a.* [:ROWNUM :rnum]]
                       :from [[{:select [:*]
                                :from [table]
                                :order-by [order]} :a]]
                       :where [:<= :ROWNUM (sql/param :end)]}]
               :where [:>= :rnum (sql/param :start)]}
        sql   (sql/format smap {:start (start p psize) :end (end p psize)})]
    (prn :projection-post params smap sql)
    (query db sql)))