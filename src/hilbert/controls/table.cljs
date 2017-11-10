(ns hilbert.controls.table
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [hiccups.core :as hiccups :refer [html]])
  (:require [cljs.core.async :as async :refer [>! <! put! chan alts!]]
            [goog.events :as events]
            [goog.dom.classes :as classes]
            [hiccups.runtime :as hiccupsrt]
            [hilbert.data.client :as data])
  (:import [goog.events EventType]))

(defn by-id
  "Shorthand for document.getElementById(id)"
  [id]
  (.getElementById js/document id))

(defn events->chan
  "Given a target DOM element and event type return a channel of
  observed events. Can supply the channel to receive events as third
  optional argument."
  ([el event-type] (events->chan el event-type (chan)))
  ([el event-type c]
   (events/listen el event-type
     (fn [e] (put! c e)))
   c))

(defn set-html!
  "Set inner HTML of element selected by element id"
  [id s]
  (set! (.-innerHTML (by-id id)) s))

(defn ->array
  [x]
  (.apply (.. js/Array -prototype -slice) x))

(defn value-map
  [rowid]
  (let [elem (.querySelector js/document (str "tr[data-id='" rowid "']"))]
    (into {} (map #(vector (keyword (.-name %)) (.-value %)) (->array (.getElementsByTagName elem "input"))))))

(defn ADD [elem]
  )

(defn UPDATE [elem rowid]
  (data/process-request [:update :fwbagnt (value-map rowid) (predicate-map rowid)]))

(defn DELETE [elem rowid])
