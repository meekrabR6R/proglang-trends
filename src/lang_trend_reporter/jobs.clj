(ns lang-trend-reporter.jobs
  (:require [clj-http.client :as client]
            [clojure.string  :as string]))

(def ^{:private true} base-url
  "https://jobs.github.com/positions.json?location=")

(def ^{:private true} langs
  ["python" "javascript"   "ruby"   "scala"  "clojure" "c"
   "c++"    "erlang"       "node"   "java"   "groovy"  "lua"
   "swift"  "objective-c"  "php"    "rust"   "golang"  "c#"
   "f#"     ".net"         "sql"    "perl"   "haskell" "scheme"
   "lisp"   "visual basic" "matlab"])

(defn get-desc-and-title
  "takes a job map and returns a new map of only the
  description and title since they are the only fields
  needed. all text is downcased"
  [{desc :description title :title}]
  {:title       (string/lower-case title)
   :description (string/lower-case desc)})

(defn get-jobs-by-city
  "returns a vector of available jobs by city"
  [city]
  (let [jobs-json (client/get (str base-url city) {:as :json})]
    (vec (:body jobs-json))))

(defn transform-jobs-vec
  "converts each item in jobs vec to new map with
  only 'description' and 'title' fields with all text downcased"
  [jarbs]
  (vec (map #(get-desc-and-title %) jarbs)))


