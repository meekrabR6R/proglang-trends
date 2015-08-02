(ns lang-trend-reporter.jobs-test
  (:require [clojure.test :refer :all]
            [lang-trend-reporter.jobs :as jobs]))


(def lang-map
  {:python       3
   :c/c++        0
   :visual-basic 0
   :swift        0
   :scheme       0
   :scala        0
   :ruby         0
   :php          0
   :perl         0
   :objective-c  0
   :node         0
   :matlab       0
   :lua          0
   :lisp         0
   :javascript   0
   :java         0
   :haskell      0
   :groovy       0
   :golang       0
   :f#           0
   :erlang       0
   :clojure      0
   :c#           0
   :.net         0})

(def job-before
  {:id            "47bfd166-36f8-11e5-8403-91cec3b60979"
   :created_at    "Thu Jul 30 20:19:50 UTC 2015"
   :title         "Software Engineer (Robotics/Mechatronics)"
   :location      "Boston, MA"
   :type          "Full Time"
   :description   "This is a job that requires Python experience."
   :how_to_apply  "Fill out an application"
   :company       "Formlabs"
   :company_url   "http://formlabs.com"
   :company_logo  "http://github-jobs.s3.amazonaws.com/411faec6-36f8-11e5-9989-ee15c863d832.jpg"
   :url           "http://jobs.github.com/positions/47bfd166-36f8-11e5-8403-91cec3b60979"})

(def job-after {:title       "software engineer (robotics/mechatronics)"
                :description "this is a job that requires python experience."})

(def job-before-vec [job-before job-before job-before])

(def job-after-vec [job-after job-after job-after])

;---------------------------- Specs for Public Functions ----------------------------
(deftest job-conversion-test
  (testing "Converting a 'job' map should create a new map with only the
           'description' and 'title' fields and all text downcased."
    (is (= (jobs/get-desc-and-title job-before)     job-after))

    (is (= (jobs/transform-jobs-vec job-before-vec) job-after-vec))))

(deftest job-contains-lang-test
  (testing "Checking if language is referenced in job should return true or false"
    (is (jobs/is-lang-in-job?      "python" {:description "machine learning with scipy!"
                                             :title       "python job"}))
    (is (jobs/is-lang-in-job?      "python" {:description "python hacking :)"
                                             :title       "software developer"})))
    (is (not (jobs/is-lang-in-job? "python" {:description "java hacking :)"
                                             :title       "software developer"}))))

(deftest lang-counts-test
  (testing "Language counts from jobs list should be a map of language counts,
            with the keys being language names, and the values the counts."
    (is (= (jobs/get-lang-counts-from-jobs job-before-vec) lang-map))))

(deftest get-jobs-by-city-test
  (testing "Getting jobs list from github api should return a vector of jobs"
    (is (vector? (jobs/get-jobs-by-city "boston")))))

(deftest percent-str-test
  (testing ""
    (is (= (jobs/langs-to-percent-str "boston" job-after-vec)
          "BOSTON:\n  - python 100.00%\n--------------------------------\n  Sourced: 3 jobs\n"))))

;---------------------------- Specs for Private Functions ----------------------------
(deftest keyword-test
  (testing "Converting a string with a space in it to a keyword
            should replace the space with a -."
    (is (= (#'jobs/as-keyword "visual basic") :visual-basic))))

(deftest regex-test
  (testing "If regex hits match in string, return true, else false."
    (is (#'jobs/contains-regex?      #"[ ]*python[.,< ]+"
                                     "the language we use is python."))
    (is (not (#'jobs/contains-regex? #"[ ]*jython[.,< ]+"
                                     "the language we use is python.")))))

(deftest city-str-from-vec
  (testing "Takes a city and vector of vectors (each of which contains two elements:
            a keyword and associated value. Converts these into a string."
    (is (#'jobs/city-string-from-vec "boston" [[:java "60%"] [:python "30%"] [:scala "10%"]])
        "BOSTON: \n  - java 60%\n  - python 30%\n  - scala 10%\n")))

