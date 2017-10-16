(ns hilbert.compiler
    (:gen-class)
    (:require [clojure.tools.reader.edn :as edn])
    (:use (hiccup.core)))

(defn control? [x]
  (and (map? x) (:control/type x)))

(defn compile-table-control
  [x]
  (let [cols (:control.table/columns x)
        labels (map :control.field/label cols)]
    [:table
     [:thead
      (map #(vector :th %) labels)]]))

(def
  ^{:dynamic true
    :doc "Dispatch table for control types, maps a keyword to a control compiler
         (a function that takes control map and generates Hiccup data to be compiled into HTML)."}
  *control-types*
  {:control.type/table compile-table-control})

; disptach on type
(defn compile-control
  [x]
  (if-let [ctrl (*control-types* (:control/type x))]
    (ctrl x)
    (throw (Exception. (str "invalid control type: " (:control/type x))))))

(defn form? [x]
  (and (map? x) (:form/name x) (:form/controls x)))

(defn compile-form [x]
  [:div {:class "form" :data-name (:form/name x)}
    (map compile-control (:form/controls x))])

(defn compile-string [s]
  (compile (edn/read-string s)))

(defn -main [file]
  (prn (compile-form (load-file file))))
