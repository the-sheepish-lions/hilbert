(ns hilbert.client
  (:require [hilbert.data.client :as c]))

(defn main []
  (.log js/console (c/projection :fwbagnt '(fwbagnt) {})))

(main)
