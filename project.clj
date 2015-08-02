(defproject lang-trend-reporter "0.1.0-SNAPSHOT"
  :description "Reports programming language trends in various US cities"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "2.0.0"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot lang-trend-reporter.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
