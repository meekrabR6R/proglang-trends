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
  "Takes a job map and returns a new map of only the
   description and title since they are the only fields
   needed. All text is downcased. What is really important
   is the downcasing (for reliable string comparison)."
  [{desc :description title :title}]
  {:title       (string/lower-case title)
   :description (string/lower-case desc)})

(defn get-jobs-by-city
  "Returns a vector of available jobs by city."
  [city]
  (let [jobs-json (client/get (str base-url city) {:as :json})]
    (vec (:body jobs-json))))

(defn transform-jobs-vec
  "Converts each item in jobs vec to new map with
   only 'description' and 'title' fields with all text downcased."
  [jarbs]
  (vec (map #(get-desc-and-title %) jarbs)))

(defn is-lang-in-job?
  "Checks if a language is referenced in a job's
   description or title."
  [lang {desc :description title :title}]
  (or (.contains desc lang) (.contains title lang)))


