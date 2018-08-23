(ns regraf.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [regraf.events :as events]
   [regraf.routes :as routes]
   [regraf.views :as views]
   [regraf.config :as config]
   [re-graph.core :as re-graph]
   ))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
