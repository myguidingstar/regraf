(ns regraf.views
  (:require
   [re-frame.core :as re-frame]
   [re-graph.core :as re-graph]
   [regraf.subs :as subs]
   [regraf.events :as events]
   [venia.core :as v]
   ))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 (str "Hello from " @name ". This is the Home Page.")]

     [:div
      [:a {:href "#/about"}
       "go to About Page"]]
     ]))


;; about

(defn about-panel []
  (let [current-game (re-frame/subscribe [::subs/current-game])]
    [:div
     [:h1 "This is the About Page."]
     [:div "Current game: " (pr-str @current-game)]
     [:div
      [:a {:href "#/"}
       "go to Home Page"]]]))

(def about-panel-with-ws
  (with-meta about-panel
    {:component-did-mount
     (fn [this]
       (re-frame/dispatch
         [::re-graph/init
          {:ws-url                  "ws://localhost:8888/graphql-ws"
           :http-url                "http://localhost:8888/graphql"
           ;; any parameters to be merged with the request, see cljs-http for options
           :http-parameters         {:with-credentials? false}
           :ws-reconnect-timeout    5000
           :resume-subscriptions?   true
           :connection-init-payload {}}])
       (re-frame/dispatch [::re-graph/subscribe
                           :my-subscription-id  ;; this id should uniquely identify this subscription
                           (v/graphql-query {:venia/queries [[:game_by_id {:id "1236"}
                                                              [:id :name :description]]]})
                           {}   ;; arguments map
                           [::events/on-thing]]))
     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::re-graph/unsubscribe :my-subscription-id]))}))

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel-with-ws]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
