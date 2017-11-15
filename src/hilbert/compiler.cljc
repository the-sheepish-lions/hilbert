(ns hilbert.compiler
    #?(:cljs (:require-macros [hiccups.core :as hiccups :refer [html]]))
    (:require [clojure.tools.reader.edn :as edn]
              #?(:clj [hilbert.data.service :as data]
                 :cljs [hilbert.data.client :as data])
              #?(:cljs [hiccups.runtime :as hiccupsrt]))
    (:use #?(:clj (hiccup.core))))

(defn compiler-error [msg]
  (throw #?(:clj (Exceptoion. msg)
            :cljs (js/Error. msg))))

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
    (compiler-error "Unknown datasource")))

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
  [x & args]
  (if-let [ctrl (@*control-types* (:control/type x))]
    (do
      (prn args)
    (apply ctrl (cons x args)))
    (compiler-error (str "invalid control type: " (:control/type x)))))

(defn form? [x]
  (and (map? x) (:form/name x) (:form/controls x)))

(defn compile-form
  "Compile form spec into a Hiccup template."
  [form params]
  [:div {:class "form" :style "padding-top: 20px" :data-name (:form/name form)}
   (if (:form/title form)
    [:h1 (:form/title form)])
    [:div {:id "alerts" :style "min-height: 50px"} "&nbsp;"]
    (for [ctrl (:form/controls form)]
      (compile-control ctrl params))])

(defn compile-string [s]
  (compile-form (edn/read-string s) {}))
