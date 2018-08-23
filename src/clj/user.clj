(ns user
  (:require
   [regraf.schema :as s]
   [com.walmartlabs.lacinia :as lacinia]
   [com.walmartlabs.lacinia.pedestal :as lp]
   [venia.core :as v]
   [io.pedestal.http :as http]))

(def schema (s/load-schema))

(defn q
  [query-string]
  (lacinia/execute schema query-string nil {}))

(comment
  (q (v/graphql-query {:venia/queries [[:game_by_id {:id "1236"}
                                        [:id :name :summary]]]})))

(defonce server nil)

(defn start-server
  [_]
  (let [server (-> schema
                 (lp/service-map {:graphiql true :subscriptions true})
                 http/create-server
                 http/start)]
    server))

(defn stop-server
  [server]
  (http/stop server)
  nil)

(defn start
  []
  (alter-var-root #'server start-server)
  :started)

(defn stop
  []
  (alter-var-root #'server stop-server)
  :stopped)

(comment
  (stop)
  (start)
)
