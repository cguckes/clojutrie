(ns clojutrie.util)

(defn dissoc-in
  [m [k & ks :as keys]]
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