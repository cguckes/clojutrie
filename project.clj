(defproject ctg/clojutrie "1.0.1"
  :description "A simple search trie built using native clojure"
  :url "https://github.com/cguckes/clojutrie"
  :min-lein-version "2.9.0"
  :license {:name "The MIT License"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :source-paths ["src"]
  :test-paths ["test"]
  :aot [clojutrie.core]
  :resource-paths ["resources"]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :signing {:gpg-key "234E9995103A0AA80B9AB8B3557812536AA7BDB1"}}]]
  :repl-options {:init-ns clojutrie.core}
  :profiles {:dev  {:dependencies [[org.clojure/test.check "1.1.0"]]}
             :test {:plugins [[lein-cloverage "1.1.2"]
                              [lein-bikeshed "0.5.2"]
                              [lein-kibit "0.1.8"]
                              [jonase/eastwood "0.3.6"]]}})
