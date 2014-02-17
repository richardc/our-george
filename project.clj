(defproject our-george "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [puppetlabs/trapperkeeper "0.3.2"]
                 [puppetlabs/trapperkeeper-webserver-jetty7 "0.3.2"]
                 [prismatic/schema "0.2.0"]
                 [slingshot "0.10.3"]]
  :repl-options {:init-ns our-george.model}
  :main puppetlabs.trapperkeeper.main)
