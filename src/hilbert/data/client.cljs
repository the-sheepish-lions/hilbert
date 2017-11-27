(ns hilbert.data.client
  (:use [clojure.string :only (join)])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [clojure.core.async :as async :refer [<! >! chan]] 
    [ajax.core :refer [ajax-request transit-request-format transit-response-format]]))

(def
  ^{:doc "Channel for communicating with the back-end"}
  service-chan (chan))

(def
  ^{:doc "Channel for receiving projections from the back-end"}
  alerts-chan (chan))

(def
  ^{:doc "Channel for receiving projections from the back-end"}
  projections-chan (chan))

(defn query-string
  [params]
  (->> params
       (map #(str (name (% 0)) "="  (% 1)))
       (join "&")
       (str "?")))

(defn handler [f]
  (fn [[ok response]]
    (if (or (not ok) (= :error (response :status)))
      (go (>! alerts-chan [:error (str (response :message))]))
      (f))))

(def debug-handler (handler #(prn %)))
(def projection-handler (handler #(go (>! projections-chan %))))
(def insert-handler (handler #(go (>! alerts-chan [:success "Record successfully added"]))))
(def update-handler (handler #(go (>! alerts-chan [:success "Record successfully updated"]))))
(def delete-handler (handler #(go (>! alerts-chan [:success "Record successfully deleted"]))))

; CRUD
; TODO: these should all accept a function argument
(defn process-projection-request
  [[tag table fields params]]
  (prn fields)
  (let [uri (str "/data/"
                 (name table)
                 (query-string
                   (assoc params :fields (->> fields (map name) (join ",")))))] 
    (prn uri)
    (ajax-request
      {:method :get
       :uri uri
       :handler projection-handler
       :format (transit-request-format)
       :response-format (transit-response-format {:keyword? true})})))

(defn process-insert-request
  [[tag table values]]
  (prn values)
  (let [uri (str "/data/" (name table))]
    (prn uri)
    (ajax-request
      {:method :post
       :uri uri
       :params {:values values}
       :handler insert-handler
       :format (transit-request-format)
       :response-format (transit-response-format {:keyword? true})})))

(defn process-update-request
  [[tag table values preds]]
  (let [uri (str "/data/" (name table))]
    (prn uri)
    (ajax-request
      {:method :put
       :uri uri
       :params {:set values :where preds}
       :handler update-handler
       :format (transit-request-format)
       :response-format (transit-response-format {:keyword? true})})))

(defn process-delete-request
  [[tag table preds]]
  (let [uri (str "/data/" (name table))]
    (prn uri)
    (ajax-request
      {:method :delete
       :uri uri
       :params {:where preds}
       :handler delete-handler
       :format (transit-request-format)
       :response-format (transit-response-format {:keyword? true})})))

; TODO: refactor, use mutiple dispatch
(defn process-request
  "Facilitates data service request dispatch. Requests are vectors of the form:
    
    [:project TABLE FIELDS PARAMS]
    [:insert TABLE VALUES]
    [:update TABLE DATA PREDICATE-MAP?]
    [:delete TABLE PREDICATE-MAP]"
  [req]
  (case (req 0)
    :project (process-projection-request req)
    :insert  (process-insert-request req)
    :update  (process-update-request req)
    :delete  (process-delete-request req)
    :else
      (throw (js/Error. (str "Invalid request: " (pr-str req))))))

(defn $ [elem] (.jQuery js/window elem))

(defn alert [kind msg]
  (str "<div class=\"alert alert-" kind "\" role=\"alert\">"
          msg
          "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">"
            "<span aria-hidden=\"true\">&times;</span>"
          "</button>"
       "</div>"))

(defmulti display-alert! (fn [[tag txt]] tag) :default :info)
(defmethod display-alert! :info [msg]
  (.append ($ "#alerts") (alert "info" (msg 1))))
(defmethod display-alert! :success [msg]
  (.append ($ "#alerts") (alert "success" (msg 1))))
(defmethod display-alert! :error [msg]
  (.append ($ "#alerts") (alert "danger" (msg 1))))
(defmethod display-alert! :warning [msg]
  (.append ($ "#alerts") (alert "warning" (msg 1))))

; process service requests
(go
  (while true
    (let [[v ch] (alts! [alerts-chan service-chan])]
      (prn :channel ch)
      (prn :value v)
      (condp = ch
        service-chan (process-request v)
        alerts-chan (display-alert! v)))))
