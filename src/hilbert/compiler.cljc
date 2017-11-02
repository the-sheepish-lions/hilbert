(ns hilbert.compiler
    (:gen-class)
    (:require [clojure.tools.reader.edn :as edn]
              [hilbert.data.service :as data])
    (:use (hiccup.core)))

(defn control? [x]
  (and (map? x) (:control/type x)))

(defn control-data-from-database-table? [ctrl]
  (let [type (:control.datasource/type ctrl)]
    (= type :datasource.type/database-table)))

(defn table-control-fields [ctrl]
  (map keyword (map :control.field/name (ctrl :control.table/columns))))

(defn control-data
  [ctrl params]
  (if (control-data-from-database-table? ctrl)
    (data/projection (keyword (:control.datasource/name ctrl)) (table-control-fields ctrl) params)
    (throw (Exception. "Unknown datasource"))))

(def
  ^{:private true
    :dynamic true
    :doc "Dispatch table for control types, maps a keyword to a control compiler
         (a function that takes control map and generates Hiccup data to be compiled into HTML)."}
  *control-types*
  (atom {}))

(defn register-control-type
  "Register control type with a function that serves as a control compiler
  e.g. (register-control-type :control.type/greet (fn [ctl] \"Hello\"))"
  [k f]
  (swap! *control-types* assoc k f))

; disptach on type
(defn compile-control
  [x]
  (if-let [ctrl (@*control-types* (:control/type x))]
    (ctrl x)
    (throw (Exception. (str "invalid control type: " (:control/type x))))))

(defn form? [x]
  (and (map? x) (:form/name x) (:form/controls x)))

(defn compile-form
  "Compile form spec into a Hiccup template."
  [form]
  [:div {:class "form" :style "padding-top: 20px" :data-name (:form/name form)}
    [:h1 (:form/name form)]
    [:div {:id "alerts" :style "min-height: 50px"} "&nbsp;"]
    (map compile-control (:form/controls form))])

(defn compile-string [s]
  (compile (edn/read-string s)))

(defn -main [file]
  (prn (compile-form (load-file file))))
