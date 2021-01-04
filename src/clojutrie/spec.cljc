(ns clojutrie.spec
  (:require [clojure.spec.alpha :as s]))

;; trie specs
(s/def ::key (s/or :sequence (s/* some?)
                   :string string?))
(s/def ::value (s/coll-of some? :kind set? :distinct true :into #{}))
(s/def ::valid-branch (s/or :val ::value
                            :trie ::trie))

(s/def ::trie (s/and (s/keys :req-un [::value])
                     (s/every-kv some?
                                 ::valid-branch)))

;; validators
(defn valid-trie? [trie]
  (let [valid (s/valid? ::trie trie)]
    (when-not valid
      (s/explain ::trie trie))
    valid))

(defn valid-value? [value]
  (let [valid (s/valid? ::value value)]
    (when-not valid
      (s/explain ::value value))
    valid))

(defn valid-key? [key]
  (let [valid (s/valid? ::key key)]
    (when-not valid
      (s/explain ::key key))
    valid))

