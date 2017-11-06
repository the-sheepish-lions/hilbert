(ns hilbert.controls.table
  (:use (hilbert.compiler)))

(defn table-control
  "A control for rendering tabular data"
  [ctrl params]
  (let [cols    (:control.table/columns ctrl)
        labels  (map :control.field/label cols)
        sorter  (:control.table/sort-by ctrl)
        psize   (:control.table/page-size ctrl)
        params* {:page      (get params :page 1)
                 :page-size (get params :page-size psize)
                 :sort-by   (get params :sort-by sorter)}
        records (hilbert.compiler/control-data ctrl params*)]
    (prn :params params params*)
    [:table {:class "table table-hover"}
     [:thead
      (map #(vector :th %) labels)]
     [:tbody
      (for [r records]
        [:tr
         (for [field r]
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
                  :type "input"
                  :value (field 1)
                  :class (field 0)}])]))])]]))

(hilbert.compiler/register-control-type :control.type/table table-control)
