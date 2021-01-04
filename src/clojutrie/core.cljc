(ns clojutrie.core
  (:require [clojure.set :as set]
            [clojure.zip :as zip]
            [clojutrie.util :as utl]
            [clojutrie.spec :as cs]
            [clojure.spec.alpha :as s]))

(defn empty-trie
  "Creates a valid empty trie."
  []
  {:post [(cs/valid-trie? %)]}
  {:value #{}})
(s/fdef empty-trie
        :ret ::cs/trie)

(defn search
  "Searches for a value in the trie with the given key. Always returns a set."
  [trie key]
  {:pre  [(cs/valid-trie? trie)
          (cs/valid-key? key)]
   :post [(cs/valid-value? %)]}
  (if-let [val (:value (get-in trie (seq key)))]
    val
    #{}))
(s/fdef search
        :args (s/cat :trie ::cs/trie :key ::cs/key)
        :ret ::cs/value)

(defn set-val
  "Sets the value with the given key."
  [trie key value-set]
  {:pre  [(cs/valid-trie? trie)
          (cs/valid-key? key)
          (cs/valid-value? value-set)]
   :post [(cs/valid-trie? %)]}
  (if (empty? key)
    (assoc trie :value value-set)
    (let [char (first key)
          child-trie (or (get trie char)
                         (empty-trie))]
      (-> trie
          (assoc :value (or (:value trie) #{}))
          (assoc char (set-val child-trie (rest key) value-set))))))

(defn insert
  "Adds a value to the given key."
  [trie key value]
  {:pre  [(cs/valid-trie? trie)
          (cs/valid-key? key)]
   :post [(cs/valid-trie? trie)]}
  (let [old-val (search trie key)
        new-val (set/union old-val #{value})]
    (set-val trie key new-val)))

(defn merge-tries
  "Merges multiple tries into one."
  [trie1 trie2]
  {:pre  [(cs/valid-trie? trie1)
          (cs/valid-trie? trie2)]
   :post [(cs/valid-trie? %)]}
  (utl/deep-merge-with
    set/union
    trie1 trie2))

(defn remove-key
  "Removes a key with all of it's values and children."
  [trie key]
  {:pre  [(cs/valid-trie? trie)
          (cs/valid-key? key)]
   :post [(cs/valid-trie? %)]}
  (utl/dissoc-in trie (seq key)))

(defn remove-key-val
  "Removes all values for a specific key."
  [trie key]
  {:pre  [(cs/valid-trie? trie)
          (cs/valid-key? key)]
   :post [(cs/valid-trie? %)]}
  (assoc-in trie (concat (seq key) [:value]) #{}))

(defn remove-val
  "Removes a value from all leaves on the trie."
  [trie val]
  {:pre  [(cs/valid-trie? trie)
          (cs/valid-value? val)]
   :post [(cs/valid-trie? %)]}
  (utl/map-cons {:value (disj (:value (utl/map-first trie)) val)}
                (utl/map-map remove-val (utl/map-rest trie) val)))

(defn- has-value? [node]
  (seq (:value node)))
(defn- paths-with-values [trie]
  (utl/map-cons (if (has-value? trie)
                  trie
                  (dissoc trie :value))
                (utl/map-map paths-with-values (utl/map-rest trie))))

(defn keywords-reduced
  [red-trie]
  (map (fn [[k v]]
         (when (map? v)
           (map #(cons k %) (keywords-reduced v))))
       red-trie)
  )

(defn keyword-for-path [node]
  (->> node
       (zip/path)
       (rest)
       (map first)
       (apply str)))

(defn keywords
  "Returns all keywords in the trie. Keywords are paths from the root to
  non-empty values."
  [trie]
  (let [zipper (->> trie
                    (paths-with-values)
                    (utl/trie-zipper))]
    (->> zipper
         (iterate zip/next)
         (take-while (complement zip/end?))
         (filter utl/leaf?)
         (map keyword-for-path))))

(defn prefix-search [trie prefix]
  (let [subtrie (get-in trie (seq prefix))]
    (->> subtrie
         (keywords)
         (map #(str prefix %))
         (set))
    ))