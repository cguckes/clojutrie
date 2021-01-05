(ns clojutrie.core-test
  (:require [clojure.test :refer :all]
            [clojutrie.core :as ct]
            [clojutrie.spec :as cs]))

(deftest empty-trie-is-valid
  (is (cs/valid-trie? (ct/empty-trie))))

(deftest set-works
  (is (= {:value #{}
          \a     {:value #{"a"}}} (ct/set-val (ct/empty-trie) "a" #{"a"})))
  (is (= {:value #{}
          \a     {:value #{"a"}}
          \b     {:value #{"b"}}}
         (-> (ct/empty-trie)
             (ct/set-val "a" #{"a"})
             (ct/set-val "b" #{"b"}))))
  (is (= {:value #{}
          \a     {:value #{}
                  \b     {:value #{"ab"}}}}
         (ct/set-val (ct/empty-trie) "ab" #{"ab"}))))

(deftest adds-value-for-one-char
  (let [trie (ct/insert (ct/empty-trie) "a" "value")]
    (is (= {:value #{}
            \a     {:value #{"value"}}} trie))))

(deftest adds-second-value-for-one-char
  (let [trie (-> (ct/empty-trie)
                 (ct/insert "a" "value1")
                 (ct/insert "a" "value2"))]
    (is (= {:value #{}
            \a     {:value #{"value2" "value1"}}} trie))))

(deftest works-on-multiple-levels
  (let [trie (-> (ct/empty-trie)
                 (ct/insert "a" "value1")
                 (ct/insert "ab" "value2"))]
    (is (= {:value #{}
            \a     {:value #{"value1"}
                    \b     {:value #{"value2"}}}}
           trie))))

(deftest finds-entry-in-simple-trie
  (is (= #{"value"}
         (ct/search {:value #{}
                     \a     {:value #{"value"}}} "a")))
  (is (= #{"value1" "value2"}
         (ct/search {:value #{}
                     \a     {:value #{"value1" "value2"}}} "a"))))

(deftest adds-a-value-for-a-key
  (let [trie (ct/insert (ct/empty-trie) "test" "value")]
    (is (= {:value #{}
            \t     {:value #{}
                    \e     {:value #{}
                            \s     {:value #{}
                                    \t     {:value #{"value"}}}}}} trie))))

(deftest search-works-on-any-level
  (let [trie {:value #{}
              \a     {:value #{"value1"}
                      \b     {:value #{"value2"}}}}]
    (is (= #{"value1"} (ct/search trie "a")))
    (is (= #{"value2"} (ct/search trie "ab")))))

(deftest merge-merges-two-tries
  (let [trie1 {:value #{}
               \a     {:value #{"a1"}
                       \b     {:value #{"ab"}
                               \c     {:value #{"abc"}}}}}
        trie2 {:value #{}
               \a     {:value #{"a2"}
                       \a     {:value #{"aa"}}}}]
    (is (= (ct/empty-trie) (ct/merge-tries (ct/empty-trie) (ct/empty-trie))))
    (is (= trie1 (ct/merge-tries (ct/empty-trie) trie1)))
    (is (= trie1 (ct/merge-tries trie1 (ct/empty-trie))))
    (is (= {:value #{}
            \a     {:value #{"a1" "a2"}
                    \a     {:value #{"aa"}}
                    \b     {:value #{"ab"}
                            \c     {:value #{"abc"}}}}}
           (ct/merge-tries trie1 trie2)))))

(deftest remove-key-deletes-every-value-in-that-branch
  (let [trie {:value #{}
              \a     {:value #{"a"}
                      \b     {:value #{"ab"}}
                      \c     {:value #{}
                              \d     {:value #{"acd"}}}}}]
    (is (= {:value #{}} (ct/remove-key {:value #{"root"}} "")))
    (is (= {:value #{}} (ct/remove-key trie "a")))
    (is (= {:value #{}
            \a     {:value #{"a"}
                    \b     {:value #{"ab"}}}}
           (ct/remove-key trie "ac")))))

(deftest remove-key-val-only-removes-specific-values
  (let [trie {:value #{}
              \a     {:value #{"a" "b"}
                      \b     {:value #{"ab" "abab"}
                              \c     {:value #{"abc"}}}}}]
    (is (= {:value #{}
            \a     {:value #{"b"}
                    \b     {:value #{"ab" "abab"}
                            \c     {:value #{"abc"}}}}}
           (ct/remove-key-val trie "a" "a")))
    (is (= {:value #{}
            \a     {:value #{"a" "b"}
                    \b     {:value #{"abab"}
                            \c     {:value #{"abc"}}}}}
           (ct/remove-key-val trie "ab" "ab")))
    (is (= trie (ct/remove-key-val trie "not-there" "blub")))))

(deftest remove-key-vals-removes-all-vals-for-a-key
  (let [trie {:value #{}
              \a     {:value #{"a"}
                      \b     {:value #{"ab"}
                              \c     {:value #{"abc"}}}}}]
    (is (= {:value #{}
            \a     {:value #{}
                    \b     {:value #{"ab"}
                            \c     {:value #{"abc"}}}}}
           (ct/remove-key-vals trie "a")))
    (is (= {:value #{}
            \a     {:value #{"a"}
                    \b     {:value #{}
                            \c     {:value #{"abc"}}}}}
           (ct/remove-key-vals trie "ab")))
    (is (= trie (ct/remove-key-vals trie "not-there")))))

(deftest remove-val-removes-specific-value-from-all-keys
  (is (= {:value #{}
          \a     {:value #{}}}
         (ct/remove-val
           (-> (ct/empty-trie) (ct/insert "a" "val"))
           "val")))
  (is (= {:value #{}
          \a     {:value #{}
                  \b     {:value #{}}}}
         (ct/remove-val
           (-> (ct/empty-trie) (ct/set-val "ab" #{}) (ct/insert "a" "val"))
           "val"))))

(deftest keywords-returns-all-inserted-words
  (is (empty? (ct/keywords (ct/empty-trie))))
  (is (= '("hi") (ct/keywords (-> (ct/empty-trie) (ct/insert "hi" 1))))))

(deftest prefix-search-returns-correct-results
  (let [trie (-> (ct/empty-trie)
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
