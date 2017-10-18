(ns hilbert.data
  (:use [clojure.java.jdbc])
  (:require [clojure.tools.reader.edn :as edn]))

(def db (:development (edn/read-string (slurp "resources/database.edn"))))
(prn (query db ["SELECT FWBAGNT_ORGN_CODE FROM FWBAGNT"]))
