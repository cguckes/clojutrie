(ns clojutrie.core
  (:require [clojure.set :as set]
            [clojutrie.util :as utl]
            [clojure.string :as str]))

(defn- chop [key]
  (map char key))

(defn search [trie key]
  (:value (get-in trie (chop key))))

(defn insert [trie key value]
  (let [old-val (search trie key)
        search-array (concat (chop key) [:value])]
    (assoc-in trie
              search-array
              (set/union old-val #{value}))))

(defn merge-tries [trie1 trie2]
  (utl/deep-merge-with set/union
                       trie1
                       trie2))

(defn remove-key [trie key]
  "Removes a key with all of it's values and children"
  (utl/dissoc-in trie (chop key)))

(defn remove-key-value [trie key]
  "Removes all values for a specific key"
  (utl/dissoc-in trie (concat (chop key) [:value])))

;(defn- concat-keys [trie]
;  (let [keys (disj (set (keys trie)) :value)]
;    (if (or (empty? keys) (nil? keys))
;      [""]
;      (map #(str % (str/join (concat-keys (get trie %)))) keys))))

(defn keywords
  ([trie]
   (keywords trie []))
  ([trie path]
   (->> (map (fn [[key value]]
               (if (and (map? value)
                        (not-empty value)
                        (not (= '(:value) (keys value))))
                 (keywords value (conj path key))
                 [(->> (conj path key)
                       (clojure.string/join)) value]))
             trie)
        (into {}))))

(defn prefix-search [trie prefix]
  (let [subtrie (get-in trie (chop prefix))]
    (->> subtrie
         (keywords)
         (keys)
         (map #(str prefix %))
         (set))
    ;(->> subtrie
    ;     (flatten-keys)
    ;     ;(concat-keys)
    ;     (map #(str prefix %))
    ;     (set))
    ))

; TODO: (defn- without-value [set value]
;  (let [new-set (disj set value)]
;    (if (nil? new-set)
;      #{}
;      new-set)))
;
;(defn remove-val [trie value]
;  (assoc trie \a (update (get \a trie) :value without-value value)))
