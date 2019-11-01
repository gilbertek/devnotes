(ns dealer-api.config)

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/dealer_dev"
   :user "admin"
   :password "admin"})
