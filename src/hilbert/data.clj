(ns hilbert.data
  (:use '[clojure.java.jdbc]))

(def db {:classname "oracle.jdbc.OracleDriver"
         :subprotocol "oracle"
         :subname "thin:@//oradev.admin.ad.cnm.edu/spc1.admin.ad.cnm.edu"
         :user "***REMOVED***"
         :password "***REMOVED***"})

(with-connection db
  (with-query-results rs ["SELECT * FROM FWBAGNT"]
    (dorun (map #(prn %) rs))))
