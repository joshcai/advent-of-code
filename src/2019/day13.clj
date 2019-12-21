(ns advent2019.day13
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day13"
       io/resource
       io/reader
       line-seq))

(def cleaned-data
  (into [] (map #(Long/parseLong %) (str/split (first data) #","))))

(defn digits [n]
  (if (pos? n)
    (conj (digits (quot n 10)) (mod n 10))
    []))

(defn parse-params [parameters]
  (let [xs (reverse (concat [0 0 0] (digits parameters)))]
    [(first xs) (second xs) (nth xs 2)]))

(defn get-default-zero [xs idx]
  (get xs idx 0))

(defn get-param-result [xs idx p base]
  (case p
    0 (get-default-zero xs (get-default-zero xs idx))
    1 (get-default-zero xs idx)
    2 (get-default-zero xs (+ (get-default-zero xs idx) base))))

(defn get-param-location [xs idx p base]
  (case p
    0 (get-default-zero xs idx)
    1 (get-default-zero xs idx)
    2  (+ (get-default-zero xs idx) base)))

(defn get-result [xs idx op parameters base]
  (let
   [[p1 p2] (parse-params parameters)
    a (get-param-result xs (+ idx 1) p1 base)
    b (get-param-result xs (+ idx 2) p2 base)]
    (op a b)))

(defn value [op a b]
  (if (op a b) 1 0))

(defn pair-with-index [xs]
  (zipmap (iterate inc 0) xs))

(defn get-color [painted-tiles x y]
  (get painted-tiles [x y] 0))

(defn update-painted-tiles [painted-tiles x y paint]
  (if (= paint (get-color painted-tiles x y))
    painted-tiles
    (assoc painted-tiles [x y] paint)))

(defn get-pos [painted-tiles tile-id]
  (first (first (filter #(= (last %) tile-id) painted-tiles))))

(defn get-ball-pos [painted-tiles] (get-pos painted-tiles 4))
(defn get-paddle-pos [painted-tiles] (get-pos painted-tiles 3))

(defn get-input [painted-tiles]
  (compare (first (get-ball-pos painted-tiles)) (first (get-paddle-pos painted-tiles))))

(defn intcode [xs-start
               idx-start
               base-start
               output-start
               painted-tiles-start ; map of tiles that have been painted with color
               ]
  (loop [xs xs-start
         idx idx-start
         base base-start
         output output-start
         painted-tiles painted-tiles-start]
    (let [parameters (quot (get-default-zero xs idx) 100) [p1 p2 p3] (parse-params parameters)]
      (case (mod (get-default-zero xs idx) 100)
        99 painted-tiles
        1 (let [r (get-result xs idx + parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base output painted-tiles))
        2 (let [r (get-result xs idx * parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base output painted-tiles))
        3 (recur (assoc xs (get-param-location xs (inc idx) p1 base) (get-input painted-tiles)) (+ idx 2) base output painted-tiles)
        4 (let [buffer (conj output (get-param-result xs (inc idx) p1 base))]
            (if (= (count buffer) 3)
              (let [[x y paint] buffer]
              (recur xs (+ idx 2) base [] (update-painted-tiles painted-tiles x y paint)))
              (recur xs (+ idx 2) base buffer painted-tiles)))
        5 (let [a (get-param-result xs (inc idx) p1 base)
                jump (not (zero? a))]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base output painted-tiles))
        6 (let [a (get-param-result xs (inc idx) p1 base)
                jump (zero? a)]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base output painted-tiles))
        7 (let [v (value <
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base output painted-tiles))
        8 (let [v (value =
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base output painted-tiles))
        9 (let [base2 (+ base (get-param-result xs (inc idx) p1 base))]
            (recur xs (+ idx 2) base2 output painted-tiles))))))

(println (count (filter
                 #(= (last %) 2)
                 (intcode (pair-with-index cleaned-data) 0 0 [] {}))))

(def part2-start 
  (assoc (pair-with-index cleaned-data) 0 2))

(println (get (intcode part2-start 0 0 [] {}) [-1 0]))