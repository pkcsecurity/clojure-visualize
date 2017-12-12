(defproject visualize "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.reader "1.1.1"]
                 [cheshire "5.8.0"]
                 [org.clojure/tools.namespace "0.3.0-alpha4"]]
  :main ^:skip-aot visualize.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
