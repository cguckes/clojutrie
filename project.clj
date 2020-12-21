(defproject ctg/clojutrie "0.0.1"
  :description "A simple search trie build on native clojure"
  :url "https://github.com/cguckes/clojutrie"
  :min-lein-version "2.9.0"
  :license {:name "The MIT License"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :source-paths ["src"]
  :test-paths ["test"]
  :aot [clojutrie.core]
  :resource-paths ["resources"]
  :deploy-repositories [["clojars" {:sign-releases false :url "https://clojars.org/repo"}]]
  :repl-options {:init-ns clojutrie.core})
