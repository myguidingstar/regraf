(ns regraf.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
   [clojure.java.io :as io]
   [com.walmartlabs.lacinia.util :as util]
   [com.walmartlabs.lacinia.schema :as schema]
   [clojure.edn :as edn]))

(defn resolve-game-by-id
  [games-map context args value]
  (let [{:keys [id]} args]
    (get games-map id)))

(defn resolver-map
  []
  (let [cgg-data  (-> (io/resource "cgg-data.edn")
                    slurp
                    edn/read-string)
        games-map (->> cgg-data
                    :games
                    (reduce #(assoc %1 (:id %2) %2) {}))]
    {:query/game-by-id (partial resolve-game-by-id games-map)}))

(defn now [] (new java.util.Date))

(defn stream-games
  [context args source-stream]
  ;; must run in a thread and return nil
  (future (while true
           (Thread/sleep 1000)
           (let [{:keys [id]} args]
             (source-stream {:id          id
                             :name        (str "game " id)
                             :description (str "description for " id " at " (now))
                             :min_players 2
                             :max_players 2}))))
  nil)

(defn load-schema
  []
  (-> (io/resource "cgg-schema.edn")
    slurp
    edn/read-string
    (util/attach-resolvers (resolver-map))
    (util/attach-streamers {:stream/game-by-id stream-games})
    schema/compile))
