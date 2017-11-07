(ns hilbert.data.client
  (:use [clojure.string :only (join)])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [clojure.core.async :as async :refer [<! >! chan]] 
    [ajax.core :refer [ajax-request]]))

(def
  ^{:doc "Channel for communicating with the back-end"}
  service-chan (chan))

(defn
  ^{:doc "Channel for receiving projections from the back-end"}
  projection-chan (chan))

(defn process-projection-request
  [[form table fields params]]
  (ajax-request
    {:method :get
     :uri (str "/data/" table "?fields=" (join "," fields))
     :handler #(go (>! projection-chan %))}))

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

(go (process-request (<! serice-chan)))

(defn projection
  [table fields params]
  (go
    (>! service-chan [:project table fields params])
    (<! projection-chan)))
