(ns lang-trend-reporter.jobs-http
  (:require [clj-http.client :as client]))

(def base-url "https://jobs.github.com/positions.json?location=")

(defn get-jobs-by-city [city]
  "returns a vector of available jobs by city"
  (let [jobs-json (client/get (str base-url city) {:as :json})]
    (vec (:body jobs-json))))
