# proglang-trends
Simple tool to view programming language trends by job listings in a set of cities

## Instructions

First, install Leiningen (see http://leiningen.org/ for instructions).

Once you have Leiningen set up, you can fun ```lein run``` from the root of the repository to run the app.

To run tests, execute ```lein test``` from root of repository.

The meat of the project is in ```./src/lang_trend_reporter/jobs.clj``` and the code that executes the program is in ```src/lang_trend_reporter/core.clj```.

(Caveat: I noticed while working on this, that calls to the api for job listings will occasionally return a 0 length array of results, when there should be results. It seems to happen randomly. The same call will return results once, and then none the next time, etc. I observed this both by making a GET request directly in the browser, as well as in the app itself.)

## Answers to Post-Project Questions

#### Challenges I ran into

The biggest challenge I ran into was probably crafting a regex that suitably hit matches for programming language references, while avoiding false positives.

#### Areas of the code that I am most proud of

I am proud of the way the program takes a vector of languages and a vector of jobs per city, and applies a series of transformations on them which results in the final output string for each city.

#### Areas of the code that I am least proud of

I am probably least proud of the hard-coded vector of programming languages. I did this for simplicity's sake, but it irks me. 

#### Tradeoffs you were forced to make

I chose to analyze the descriptions and titles of the listings with a regular expression to detect matches for languages (each listing that has a match increments the language count by one for the city the listings are for). The alternative would've probably been to use the 'description' query parameter. This would've required making an api request for each language for each city, which would've been an absurd number of network requests. So, instead, I opted for a somewhat imperfect regular expression, which allowed me to only have to make one network request per city.
