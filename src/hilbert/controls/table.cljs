(ns hilbert.controls.table
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [hiccups.core :as hiccups :refer [html]])
  (:require [cljs.core.async :as async :refer [>! <! put! chan alts!]]
            [clojure.string :refer [join]]
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
    (into {} (map #(vector (keyword (.-name %)) (.-value %)) (->array (.querySelectorAll elem "input[type=text]"))))))

(defn predicate-map
  [rowid]
  (let [elem (.querySelector js/document (str "tr[data-id='" rowid "']"))]
    (into {} (map #(vector (keyword (.-name %)) (.-value %)) (->array (.querySelectorAll elem "input[type=hidden]"))))))

(defn row-template []
  (let [elem  (.querySelector js/document (str "tr[data-id='0']"))
        cells (->array (.querySelectorAll elem "td"))]
    (for [cell cells]
      (if-let [in (.querySelector cell "input")]
        [:input (.-name in)]
        [:blank]))))

(def new-row-id (atom 0))

(defn row-html []
  (swap! new-row-id dec)
  (str "<tr data-id=\"" @new-row-id "\">"
       "<td><div class=\"btn-group\">"
       "<a class=\"btn btn-sm btn-primary\" onclick=\"hilbert.controls.table.INSERT(this, " @new-row-id ")\" href=\"#\">Update</a>"
       "<a class=\"btn btn-sm btn-secondary\" onclick=\"hilbert.controls.table.DELETE_NEW(this, " @new-row-id ")\" href=\"#\">Delete</a>"
       "</div></td>"
       (->> (row-template)
            (map #(case (% 0) :input (str "<input type=\"text\" name=\"" (% 1) "\">") :blank ""))
            (join "</td><td>"))
       "</tr>"))

(def $ (.-jQuery js/window))

(defn ADD [elem]
  (.prepend ($ "tbody") (row-html)))

(defn INSERT [elem rowid]
  (data/process-request [:insert :fwbagnt [(value-map rowid)]]))

(defn DELETE_NEW [elem rowid]
  (.fadeOut ($ (str "tr[data-id=" rowid "]"))))

(defn UPDATE [elem rowid]
  (data/process-request [:update :fwbagnt (value-map rowid) (predicate-map rowid)]))

(defn DELETE [elem rowid]
  (data/process-request [:delete :fwbagnt (predicate-map rowid)]))
