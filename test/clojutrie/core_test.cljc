(ns clojutrie.core-test
  (:require [clojure.test :refer :all]
            [clojutrie.core :as ct]))

(deftest adds-value-for-one-char
  (let [trie (ct/insert nil "a" "value")]
    (is (= {\a {:value #{"value"}}} trie))))

(deftest adds-second-value-for-one-char
  (let [trie (-> {}
                 (ct/insert "a" "value1")
                 (ct/insert "a" "value2"))]
    (is (= {\a {:value #{"value2" "value1"}}} trie))))

(deftest works-on-multiple-levels
  (let [trie (-> {}
                 (ct/insert "a" "value1")
                 (ct/insert "ab" "value2"))]
    (is (= {\a {\b     {:value #{"value2"}}
                :value #{"value1"}}}
           trie))))

(deftest finds-entry-in-simple-trie
  (is (= #{"value"}
         (ct/search {\a {:value #{"value"}}} "a")))
  (is (= #{"value1" "value2"}
         (ct/search {\a {:value #{"value1" "value2"}}} "a"))))

(deftest adds-a-value-for-a-key
  (let [trie (ct/insert nil "test" "value")]
    (is (= {\t {\e {\s {\t {:value #{"value"}}}}}} trie))))

(deftest search-works-on-any-level
  (let [trie {\a {\b     {:value #{"value2"}}
                  :value #{"value1"}}}]
    (is (= #{"value1"} (ct/search trie "a")))
    (is (= #{"value2"} (ct/search trie "ab")))))

(deftest merge-merges-two-tries
  (let [trie1 {\a {\b     {\c     {:value #{"abc"}}
                           :value #{"ab"}}
                   :value #{"a1"}}}
        trie2 {\a {\a     {:value #{"aa"}}
                   :value #{"a2"}}}]
    (is (= {} (ct/merge-tries {} {})))
    (is (= trie1 (ct/merge-tries {} trie1)))
    (is (= trie1 (ct/merge-tries trie1 {})))
    (is (= {\a {\a     {:value #{"aa"}}
                \b     {\c     {:value #{"abc"}}
                        :value #{"ab"}}
                :value #{"a1" "a2"}}}
           (ct/merge-tries trie1 trie2)))))

(deftest remove-key-deletes-every-value-in-that-branch
  (let [trie {\a {\b     {:value "ab"}
                  \c     {\d {:value "acd"}}
                  :value "a"}}]
    (is (= {} (ct/remove-key trie "a")))
    (is (= {\a {\b     {:value "ab"}
                :value "a"}}
           (ct/remove-key trie "ac")))))

(deftest remove-key-value-only-removes-specific-values
  (let [trie {\a {\b     {:value "ab"
                          \c     {:value "abc"}}
                  :value "a"}}]
    (is (= {\a {\b {:value "ab"
                    \c     {:value "abc"}}}}
           (ct/remove-key-value trie "a")))
    (is (= {\a {\b     {\c {:value "abc"}}
                :value "a"}}
           (ct/remove-key-value trie "ab")))))

(deftest prefix-search-returns-correct-results
  (let [trie (-> {}
                 (ct/insert "hi there" 1)
                 (ct/insert "hi yourself" 2)
                 (ct/insert "see you" 3))]
    (is (= #{"hi there"
             "hi yourself"}
           (ct/prefix-search trie "h")))
    (is (= #{"hi there"
             "hi yourself"}
           (ct/prefix-search trie "hi ")))
    (is (= #{"see you"}
           (ct/prefix-search trie "s")))
    ))

; TODO: (deftest remove-val-removes-specific-value-from-all-keys
;  (is (= {\a {:value #{}}} (ct/remove-val {\a {:value ["val"]}} "val")))
;  (is (= {\a {\b     {}
;              :value []}} (ct/remove-val {\a {\b     {}
;                                              :value ["val"]}} "val"))))
