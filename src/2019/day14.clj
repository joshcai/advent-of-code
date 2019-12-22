(ns advent2019.day14
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day14"
       io/resource
       io/reader
       line-seq))

(defn parse-nums [s]
  (map (fn [n] (Long/parseLong n)) (re-seq #"\d+" s)))

(defn parse-letters [s]
  (re-seq #"[A-Z]+" s))

(defn create-entry [nums letters]
  (let [part-nums (drop-last nums)
        part-letters (drop-last letters)]
    [(last letters)
     {:count (last nums)
      :parts (zipmap part-letters part-nums)}]))

;; Format:
;; map where key is the output, with value:
;;  another map with {:count <count> :parts <map>}

(def cleaned-data 
  (reduce (fn [coll curr] 
            (let [
                  [letter parts] 
                  (create-entry (parse-nums curr) (parse-letters curr))
                  ]
              (assoc coll letter parts))) {} data))

(defn ore-required [data element want-count remainder]
  (let [curr (get data element)
        count (:count curr)
        parts (:parts curr)]
    (if (or (= element "ORE") (zero? want-count)) 
      [want-count remainder]
      (reduce
       (fn [coll pair]
         (let [[old-ore old-remainder] coll
               actual-want-count (- want-count (get remainder element 0))
               multiplier (long (Math/ceil (/ actual-want-count count)))
               new-remainder-amount (- (* multiplier count) actual-want-count)
               [new-ore new-remainder]
               (ore-required data (first pair) (* multiplier (second pair))
                             (assoc old-remainder element new-remainder-amount))]
           [(+ new-ore old-ore) new-remainder]))
       [0 remainder]
       parts))))

(def one-ore-required (ore-required cleaned-data "FUEL" 1 {}))
(def min-ore-required (first one-ore-required))
(println (first one-ore-required))

(defn bin-search [min-start max-start compare-fn]
  (loop [min min-start
         max max-start]
    (let [curr (long (/ (+ max min) 2))]
      (case (compare-fn curr)
        -1 (recur curr max)
        0 curr
        1 (recur min curr)))))

(defn max-fuel [ore-supply]
  (bin-search 0 ore-supply 
              (fn [x] 
                (let [[ore1 _] (ore-required cleaned-data "FUEL" x {})
                      [ore2 _] (ore-required cleaned-data "FUEL" (inc x) {})]
                  (if (and (<= ore1 ore-supply) (> ore2 ore-supply)) 0
                        (if (<= ore2 ore-supply) -1 1))))))

(println (max-fuel 1000000000000))
