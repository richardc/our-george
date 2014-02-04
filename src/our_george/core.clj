(ns our-george.core
  (:require [compojure.core :refer [context]]
            [puppetlabs.trapperkeeper.core :refer [defservice]]
            [compojure.core :refer [defroutes GET PUT ANY]]
            [compojure.route :as route]
            [clojure.tools.logging :as log]))

(defroutes app
  (GET "/" [] "Hello world")
  (route/resources "/")
  (route/not-found "Not found"))

(defn on-shutdown
  []
  (log/info "Bye bye from Our George."))

(defservice our-george-service
  {:depends [[:config-service get-config]
             [:webserver-service add-ring-handler]]
   :provides [shutdown]}
  (let [config (get-config)]
    (add-ring-handler app "/"))
  {:shutdown on-shutdown})
