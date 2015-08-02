(ns lang-trend-reporter.jobs-test
  (:require [clojure.test :refer :all]
            [lang-trend-reporter.jobs :as jobs]))

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

(deftest job-conversion-test
  (testing "Converting a 'job' map should create a new map with only the
           'description' and 'title' fields and all text downcased."
    (is (= (jobs/get-desc-and-title job-before) job-after))
    (is (= (jobs/transform-jobs-vec job-before-vec) job-after-vec))))

(deftest job-contains-lang-test
  (testing ""
    (is (jobs/is-lang-in-job? "python" {:description "machine learning with scipy!"
                                        :title "python job"}))
    (is (jobs/is-lang-in-job? "python" {:description "python hacking :)"
                                        :title "software developer"})))
    (is (not (jobs/is-lang-in-job? "python" {:description "java hacking :)"
                                             :title "software developer"}))))
