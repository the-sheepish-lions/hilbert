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
  projection-chan (chan))

(def
  ^{:doc "Channel for receiving projections from the back-end"}
  alerts-chan (chan))

(defn query-string
  [params]
  (->> params
       (map #(str (name (% 0)) "="  (% 1)))
       (join "&")
       (str "?")))

(defn debug-handler
  [[ok response]]
  (if ok
    (prn response)
    (.error js/console (prn-str response)))
  [ok response])

(defn projection-handler
  [[ok response]]
  (if ok
    (go (>! projection-chan response))
    (go (>! alerts-chan [:error (str response)]))))

; CRUD
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
       :handler debug-handler
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
       :handler debug-handler
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
       :handler debug-handler
       :format (transit-request-format)
       :response-format (transit-response-format {:keyword? true})})))

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

; process service requests
(go
  (while true
    (let [[v ch] (alts! [projection-chan alerts-chan service-chan])]
      (prn :channel ch)
      (prn :value v)
      (condp = ch
        service-chan (process-request v)
        alerts-chan (.error js/console v)
        projection-chan (.log js/console v)))))

(defn project!
  [table fields params]
  (go (>! service-chan [:project table fields params])))
