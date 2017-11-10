(ns hilbert.util)

(defn string->int [x]
  (if (integer? x)
    x
    (Integer/parseInt x)))

(defn string->keyword [x]
  (if (keyword? x)
    x
    (keyword x)))
