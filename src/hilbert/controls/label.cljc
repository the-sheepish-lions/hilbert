(ns hilbert.controls.label
  (:require [hilbert.compiler :as compiler :refer [register-control-type]]))

(defn label
  [ctrl params]
  (if (ctrl :control/name)
    (str "<div class=\"hilbert-label\" id=\"" (ctrl :control/name) "\">" (ctrl :control.label/text) "</div>")
    (str "<div class=\"hilber-label\">" (ctrl :control.label/text) "</div>")))

(register-control-type :control.type/label label)
