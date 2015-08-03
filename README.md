# proglang-trends
Simple tool to view programming language trends by job listings in a set of cities

## Instructions

First, install Leiningen (see http://leiningen.org/ for instructions).

Once you have Leiningen set up, you can fun ```lein run``` from the root of the repository to run the app.

To run tests, execute ```lein test``` from root of repository.

(Caveat: I noticed while working on this, that calls to the api for job listings will occasionally return a 0 length array of results, when there should be results. It seems to happen randomly. The same call will return results once, and then none the next time, etc. I observed this both by making a GET request directly in the browser, as well as in the app itself.)
