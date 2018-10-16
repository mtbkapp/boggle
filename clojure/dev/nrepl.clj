(ns nrepl
  (:require [clojure.tools.nrepl.server :refer [start-server stop-server]])
  (:import [java.net ServerSocket]))


(def server nil)

(defn server->url
  [{^ServerSocket socket :server-socket}]
  (str "nrepl://"
       (.getHostAddress (.getInetAddress socket))
       ":"
       (.getLocalPort socket)))

(defn -main
  [& args]
  (alter-var-root #'server (constantly (start-server :bind "localhost" :port 0)))
  (require '[clojure.repl])
  (println "Listening at: " (server->url server)))

