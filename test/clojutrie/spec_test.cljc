(ns clojutrie.spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojutrie.spec :as cs]
            [clojure.spec.test.alpha :as stest]))

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

(deftest functions-adhere-to-spec
  (is (passed? 'clojutrie.core/empty-trie) "empty-trie failed spec test")
  (is (passed? 'clojutrie.core/search) "search failed spec test")
  (is (passed? 'clojutrie.core/set-val) "set-val failed spec test")
  (is (passed? 'clojutrie.core/insert) "insert failed spec test")
  (is (passed? 'clojutrie.core/merge-tries) "merge-tries failed spec test")
  (is (passed? 'clojutrie.core/remove-key) "remove-key failed spec test")
  (is (passed? 'clojutrie.core/remove-key-val) "remove-key-val failed spec test")
  (is (passed? 'clojutrie.core/remove-key-vals) "remove-key-val failed spec test")
  (is (passed? 'clojutrie.core/remove-val) "remove-val failed spec test")
  (is (passed? 'clojutrie.core/keywords) "keywords failed spec test")
  (is (passed? 'clojutrie.core/prefix-search) "prefix-search failed spec test"))
