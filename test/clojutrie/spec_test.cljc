(ns clojutrie.spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojutrie.spec :as cs]
            [clojure.spec.test.alpha :as stest]
            [clojutrie.core :as ct]))

(deftest spec-accepts-valid-tries
  (is (s/valid? ::cs/trie {:value #{}}))
  (is (s/valid? ::cs/trie {:value #{"a" "b"}}))
  (is (s/valid? ::cs/trie {:value #{"a" "b"}
                           \a     {:value #{"a"}}})))

(deftest spec-rejects-invalid-tries
  (is (not (s/valid? ::cs/trie nil)))
  (is (not (s/valid? ::cs/trie {})))
  (is (not (s/valid? ::cs/trie {:value []})))
  (is (not (s/valid? ::cs/trie {:value ["a" "b"]})))
  (is (not (s/valid? ::cs/trie {:value ["a" "a"]})))
  (is (not (s/valid? ::cs/trie {\a {:value #{}}}))))

; TODO
(deftest functions-are-speced-correctly
  (let [val (stest/check 'ct/empty-trie)]
    (println val)
    (is val)))
