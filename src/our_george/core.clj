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

(defservice our-george-service
  [[:WebserverService add-ring-handler]]
  (init [this context]
        (log/info "Hello from Our George.")
        (add-ring-handler app "/")
        context)
  (stop [this context]
        (log/info "Bye bye from Our George.")
        context))
