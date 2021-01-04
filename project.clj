(defproject ctg/clojutrie "0.0.2"
  :description "A simple search trie built using native clojure"
  :url "https://github.com/cguckes/clojutrie"
  :min-lein-version "2.9.0"
  :license {:name "The MIT License"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/test.check "1.1.0"]]
  :source-paths ["src"]
  :test-paths ["test"]
  :aot [clojutrie.core]
  :resource-paths ["resources"]
  :deploy-repositories [["clojars" {:sign-releases false :url "https://clojars.org/repo"}]]
  :repl-options {:init-ns clojutrie.core})
