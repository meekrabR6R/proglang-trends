(ns lang-trend-reporter.jobs-http
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

(defn get-jobs-by-city [city]
  "returns a vector of available jobs by city"
  (let [jobs-json (client/get (str base-url city) {:as :json})]
    (vec (:body jobs-json))))
