(ns regraf.events
  (:require
   [re-frame.core :as re-frame]
   [regraf.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  ::on-thing
  (fn [db [_ {:keys [data errors] :as payload}]]
    (let [game (:game_by_id data)]
      (assoc db :current-game game))))
