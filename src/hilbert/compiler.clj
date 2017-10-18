(ns hilbert.compiler
    (:gen-class)
    (:require [clojure.tools.reader.edn :as edn])
    (:use (hiccup.core)))

(def form1
  {:form/name "FWVAGNT",
   :form/controls
    [{:control/type :control.type/table,
      :control.datasource/name "FWBAGNT"
      :control.datasource/type :datasource.type/database-table
      :control.table/page-size 40
      :control.table/insert-records? true ; can insert records
      :control.table/remove-records? true
      :control.table/columns
        [{:control.field/name "FWBAGNT_ORGN_CODE"
          :control.field/required? true
          :control.field/readonly? false
          :control.field/spec string?
          :control.field/help "Enter Origin Code"
          :control.field/label "Orgn"}
         {:control.field/name "FWBAGNT_CONTROL_AGENT1"
          :control.field/required? true
          :control.field/readonly? false
          :control.field/spec string?
          :control.field/help "Enter Control Agent 1"
          :control.field/label "Control Agent 1"}
         {:control.field/name "FWBAGNT_CONTROL_AGENT2"
          :control.field/required? true
          :control.field/readonly? false
          :control.field/spec string?
          :control.field/help "Enter Control Agent 2"
          :control.field/label "Control Agent 2"}
         {:control.field/name "FWBAGNT_CONTROL_AGENT3"
          :control.field/required? true
          :control.field/readonly? false
          :control.field/spec string?
          :control.field/help "Enter Control Agent 3"
          :control.field/label "Control Agent 3"}]}]})

(defn control? [x]
  (and (map? x) (:control/type x)))

(defn compile-table-control
  [x]
  (let [cols (:control.table/columns x)
    [:table
     [:thead
      (map #(vector :th %) labels)]]))

(def
  ^{:private true
    :dynamic true
    :doc "Dispatch table for control types, maps a keyword to a control compiler
         (a function that takes control map and generates Hiccup data to be compiled into HTML)."}
  *control-types*
  {:control.type/table compile-table-control})

(defn register-control-type
  "Register control type with a function that serves as a control compiler
  e.g.
    (register-control-type :control.type/greet (fn [ctl] \"Hello\"))"
  [k f]
  (set! *control-types* (assoc *control-types* k f)))

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
