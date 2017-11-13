(ns hilbert.util)

(defn string->int [x]
  (if (integer? x)
    x
    #?(:clj (Integer/parseInt x)
       :cljs (js/parseInt x))))

(defn string->keyword [x]
  (if (keyword? x)
    x
    (keyword x)))
