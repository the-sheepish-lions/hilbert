(ns hilbert.controls.table
  #?(:cljs
     (:require-macros [cljs.core.async.macros :refer [go]]
                      [hiccups.core :as hiccups :refer [html]]))
  (:require
    #?(:cljs [cljs.core.async :as async :refer [>! <! put! chan alts!]])
    #?(:cljs [goog.events :as events])
    #?(:cljs [goog.dom.classes :as classes])
    #?(:cljs [hiccups.runtime :as hiccupsrt])
    #?(:cljs [hilbert.data.client :as data])
    [hilbert.compiler :as compiler :refer [register-control-type control-data]]
    [hilbert.util :as util :refer [string->int string->keyword]])
  #?(:cljs (:import [goog.events EventType])))

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

(defn ADD [elem]
  )

(defn UPDATE [elem rowid]
  (data/process-request [:update :fwbagnt (value-map rowid) (predicate-map rowid)]))

(defn DELETE [elem rowid]
  (data/process-request [:delete :fwbagnt (predicate-map rowid)]))

(defn table-control
  "A control for rendering tabular data"
  [ctrl params]; TODO [ctrl data params]
  (let [cols    (:control.table/columns ctrl)
        labels  (map :control.field/label cols)
        sorter  (:control.table/sort-by ctrl)
        psize   (:control.table/page-size ctrl)
        page    (string->int (get params :page 1))
        params* {:page      page
                 :page-size (string->int (get params :page-size psize))
                 :sort-by   (string->keyword (get params :sort-by sorter))}
        proj    (hilbert.compiler/control-data ctrl params*) ; TODO: data
        pcount  (int (Math/ceil (/ (proj :count) psize)))]
    (prn :params params params*)
    [:div.table-control
     [:div.row {:style "padding-bottom: 10px"}
      [:a.btn.btn-primary
       {:href "#"
        :title "Add Record"
        :onclick (str "hilbert.controls.table.ADD(this)")} "Add"]]
     [:div.row
      [:table.table.table-hover
       [:thead
        [:th "#"]
        (map #(vector :th %) labels)]
       [:tbody
        (for [r (map-indexed #(vector %1 %2) (proj :data))]
          [:tr {:data-id (r 0)}
           [:td
            [:div.btn-group
             [:a.btn.btn-sm.btn-primary
              {:href "#"
               :title "Update Record"
               :onclick (str "hilbert.controls.table.UPDATE(this, " (r 0) ")")}
              "Update"]
             [:a.btn.btn-sm.btn-secondary
              {:href "#"
               :title "Delete Record"
               :onclick (str "hilbert.controls.table.DELETE(this, " (r 0) ")")}
              "Delete"]]]
           (for [field (r 1)]
             (let [nm    (field 0)
                   data  (->> cols (filter #(= (:control.field/name %) nm)) first)
                   ro?   (if data (:control.field/read-only? data))
                   width (if data (:control.field/width data))
                   help  (if data (:control.field/help data))]
               [:td {:class (field 0) :title help}
                (if ro?
                  (field 1)
                  [:input
                   {:placeholder help
                    :style (if width (str "width: " width))
                    :type "text"
                    :name  (field 0)
                    :value (field 1)
                    :class (field 0)}])
                  [:input
                   {:type "hidden"
                    :name  (field 0)
                    :value (field 1)}]]))])]]]
      [:div.row
       ; TODO: make pagination truncate when there are too many pages
       [:br]
       [:nav
        [:ul.pagination
         [:li.page-item
          {:class (if (= page 1) "disabled")}
          [:a.page-link
           {:href (str "?page=" (if (= page 1) page (dec page)))} "&lt;&lt;"]]
         (for [p (range 1 (inc pcount))]
           [:li.page-item
            {:class (if (= p page) "active")}
            [:a.page-link
             {:href (str "?page=" p)} p]])
         [:li.page-item
          {:class (if (= page pcount) "disabled")}
          [:a.page-link
           {:href (str "?page=" (if (= page pcount) page (inc page)))} "&gt;&gt;"]]]]]]))

(hilbert.compiler/register-control-type :control.type/table table-control)
