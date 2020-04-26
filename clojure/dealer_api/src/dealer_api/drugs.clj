(ns dealer-api.drugs
    (:require [dealer-api.sql.drugs :as sql]
              [dealer-api.config :refer [db]]
              [io.pedestal.http :as http]))

(defn all-drugs [_]
  (http/json-response (sql/drugs db)))

(do
  (require '[dealer-api.config :refer [db]])
  (require '[dealer-api.sql.drugs :as sd] :reload)
  (require '[dealer-api.drugs :as d] :reload))
