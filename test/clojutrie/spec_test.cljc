(ns clojutrie.spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojutrie.spec :as cs]
            [clojure.spec.test.alpha :as stest]
            [clojutrie.core]))

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

(defn- passed? [func]
  (-> (stest/check func)
      (first)
      (:clojure.spec.test.check/ret)
      (:pass?)))
(defn- failed [func]
  (str func " failed spec test"))
(deftest functions-adhere-to-spec
  (is (passed? 'clojutrie.core/empty-trie) (failed "empty-trie"))
  (is (passed? 'clojutrie.core/search) (failed "search"))
  (is (passed? 'clojutrie.core/set-val) (failed "set-val"))
  (is (passed? 'clojutrie.core/insert) (failed "insert"))
  (is (passed? 'clojutrie.core/merge-tries) (failed "merge-tries"))
  (is (passed? 'clojutrie.core/remove-key) (failed "remove-key"))
  (is (passed? 'clojutrie.core/remove-key-val) (failed "remove-key-val"))
  (is (passed? 'clojutrie.core/remove-key-vals) (failed "remove-key-val"))
  (is (passed? 'clojutrie.core/remove-val) (failed "remove-val"))
  (is (passed? 'clojutrie.core/keywords) (failed "keywords"))
  (is (passed? 'clojutrie.core/prefix-search) (failed "prefix-search")))
