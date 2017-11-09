(ns hilbert.client
  (:require [hilbert.data.client :as c]))

(enable-console-print!)

(defn main []
  (.log js/console (c/projection :fwbagnt '(fwbagnt) {})))

(main)
