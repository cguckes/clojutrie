(ns clojutrie.util
  (:require [clojure.zip :as zip]))

(defn dissoc-in
  [m [k & ks]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defn deep-merge-with [f & maps]
  (apply
    (fn merge-method [& maps]
      (if (every? map? maps)
        (apply merge-with merge-method maps)
        (apply f maps)))
    maps))

(defn leaf? [node]
  (let [result (-> (zip/node node) (second) (set?))]
    result))

(defn- children [node]
  (if (map? node)
    (map seq (dissoc node :value))
    (if (= :value (first node))
      '()
      (second node))))
(defn trie-zipper [trie]
  (zip/zipper
    (fn [_] true)
    children
    identity
    trie))

(defn map-first [map]
  (let [head (first (keys map))]
    {head (get map head)}))

(defn map-rest [map]
  (dissoc map (first (keys map))))

(defn map-cons [map1 map2]
  (into {} (concat map1 map2)))

(defn map-map [f m & args]
  (into {} (map (fn [k] {k (apply f (get m k) args)})
                (keys m))))
