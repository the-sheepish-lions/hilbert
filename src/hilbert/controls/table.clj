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
        proj    (hilbert.compiler/control-data ctrl params*)
        pcount  (Math/ceil (/ (proj :count) psize))]
    (prn :count (proj :count))
    (prn :params params params*)
    [:div.table-control
     [:div.row {:style "padding-bottom: 10px"}
      [:a.btn.btn-primary {:href "#" :onclick (str "hilbert.controls.table.ADD()")} "Add"]]
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
             [:a.btn.btn-sm.btn-primary {:href "#" :onclick (str "hilbert.controls.table.UPDATE(" (r 0) ")")} "Update"]
             [:a.btn.btn-sm.btn-secondary {:href "#" :onclick (str "hilbert.controls.table.DELETE(" (r 0) ")")} "Delete"]]]
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
                    :type "input"
                    :value (field 1)
                    :class (field 0)}])]))])]]]
      [:div.row
       ; TODO: make pagination truncate when there are too many pages
       [:br]
       [:nav
        [:ul.pagination
         [:li.page-item [:a.page-link {:href "#"} "<<"]]
         (for [p (range 1 (inc pcount))]
           [:li.page-item [:a.page-link {:href (str "#?p=" p)} p]])
         [:li.page-item [:a.page-link {:href "#"} ">>"]]]]]]))

(hilbert.compiler/register-control-type :control.type/table table-control)
