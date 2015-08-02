(ns lang-trend-reporter.core
  (:require [lang-trend-reporter.jobs :as job])
  (:gen-class))

(defn -main
  "Prints breakdowns of programming language popularity by city
   based on references in jobs listings."
  [& args]
  (println (job/format-lang-trends-str "boston "))
  (println (job/format-lang-trends-str "san francisco"))
  (println (job/format-lang-trends-str "los angeles"))
  (println (job/format-lang-trends-str "denver"))
  (println (job/format-lang-trends-str "boulder"))
  (println (job/format-lang-trends-str "chicago")))

