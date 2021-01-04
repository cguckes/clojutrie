(ns clojutrie.util-test
  (:require [clojure.test :refer :all]
            [clojutrie.util :refer :all]))

(def test-map {:a 1
               :b 2
               :c 3
               \d 4
               \e 5})

(deftest map-helpers-work-as-expected
  (is (= {:a 1}
         (map-first test-map)))
  (is (= {:b 2 :c 3 \d 4 \e 5}
         (map-rest test-map)))
  (is (= test-map
         (map-cons {:a 1} {:b 2 :c 3 \d 4 \e 5})))
  (is (= {:a 2 :b 3 :c 4 \d 5 \e 6}
         (map-map (fn [orig] (+ orig 1)) test-map)))
  (is (= {:a 3 :b 4 :c 5 \d 6 \e 7}
         (map-map (fn [orig add] (+ orig add)) test-map 2))))