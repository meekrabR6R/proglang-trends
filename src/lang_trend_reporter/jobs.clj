(ns lang-trend-reporter.jobs
  (:require [clj-http.client   :as client]
            [clojure.string    :as string]
            [clojure.data.json :as json]))

;------------------------ Private -------------------------
(def ^{:private true} langs
  ;;hard-coded for convenience. this would come from some external
  ;;resource (e.g. text file, api, etc..)
  ["python" "javascript"   "ruby"    "scala"   "clojure" "c/c++"
   "perl"   "erlang"       "node"    "java"    "groovy"  "lua"
   "swift"  "objective-c"  "php"     "golang"  "c#"      "matlab"
   "f#"     ".net"         "haskell" "scheme"  "lisp"    "visual basic"])

(defn- as-keyword
  "Convenience function for converting lang strings to keywords"
  [x]
  (keyword (string/replace (string/trim x) " " "-" )))

(defn- contains-regex?
  "Returns true if regex hits a match in string,
  false otherwise."
  [regex strng]
  (let [match-count (count (re-find regex strng))]
    (> match-count 0)))

(defn- sort-by-count
  [items]
  (into (sorted-map-by (fn [key1 key2]
                       (compare [(get items key2) key2]
                                [(get items key1) key1])))
        items))

(defn- city-string-from-vec
  "Create formatted string for city and its programming trends"
  [city v]
  (str (string/upper-case city) ":\n"
       (string/replace (string/join v) ":" "  - ")))

;------------------------ Public --------------------------
(defn get-jobs-by-city
  "Returns a vector of available jobs by city."
  [city]
  (let [base-url "https://jobs.github.com/positions.json?location="
        jobs-json (client/get (str base-url city) {:as :json})]
    (json/read-str (:body jobs-json) :key-fn keyword)))

(defn get-desc-and-title
  "Takes a job map and returns a new map of only the
   description and title since they are the only fields
   needed. All text is downcased. What is really important
   is the downcasing (for easier regex matching)."
  [{desc :description title :title}]
  {:title       (string/lower-case title)
   :description (string/lower-case desc)})

(defn transform-jobs-vec
  "Converts each item in jobs vec to new map with
   only 'description' and 'title' fields with all text downcased."
  [jarbs]
  (vec (map #(get-desc-and-title %) jarbs)))

(defn is-lang-in-job?
  "Checks if a language is referenced in a job's
   description or title."
  [lang {desc :description title :title}]
  (let [regex (re-pattern
                (str "[ ]*"
                     (string/replace lang "++" "\\+\\+") "[.,< ]+"))]
    (or (contains-regex? regex desc)
        (contains-regex? regex title))))

(defn get-lang-counts-from-jobs
  "Takes a map of jobs and returns a map with language
   names as keywords and language counts as values"
  [jobs]
  (let [jarbs (transform-jobs-vec jobs)]
    (sort-by-count
      (into {}
            (map (fn [lang]
                   {(as-keyword lang)
                    (reduce + (vec
                                (map #(cond
                                        (is-lang-in-job? lang %) 1
                                        :else 0)
                                   jarbs)))})
                langs)))))

(defn langs-to-percent-str
  "Takes a city name and jobs vector and returns a formatted string of
   programming languages sorted by percentage."
  [city jobs]
  (let [ks                (vec (map #(as-keyword %) langs))
        lang-counts-map   (get-lang-counts-from-jobs jobs)
        values            (vec (map #(% lang-counts-map) ks))
        values-total      (reduce + values)
        perc-str-vec      (transient [])]
    (doseq [kv (seq lang-counts-map)]
      (conj!
        perc-str-vec
        (if (> (get kv 1) 0)
          (str (string/join " "
                 [(get kv 0)
                  (format "%.2f"(* 100 (/ (get kv 1) (float values-total))))])
                "%\n")
            "")))
    (str (city-string-from-vec city (persistent! perc-str-vec))
         "--------------------------------\n"
         "  Sourced: " (count jobs) " jobs\n")))


(defn format-lang-trends-str
  "Convenience function for easily generating formatted language
  trend string by city"
  [city]
  (langs-to-percent-str city (get-jobs-by-city city)))
