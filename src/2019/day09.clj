(ns advent2019.day09
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day09"
       io/resource
       io/reader
       line-seq))

(def cleaned-data
  (into [] (map #(Integer/parseInt %) (str/split (first data) #","))))

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

(defn intcode [xs-start idx-start input-start output-start base-start]
  (loop [xs xs-start
         idx idx-start
         input input-start
         output output-start
         base base-start]
  (let [parameters (quot (get-default-zero xs idx) 100) [p1 p2 p3] (parse-params parameters)]
    (case (mod (get-default-zero xs idx) 100)
      99 output
      1 (let [r (get-result xs idx + parameters base)]
          (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) input output base))
      2 (let [r (get-result xs idx * parameters base)]
          (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) input output base))
      3 (recur (assoc xs (get-param-location xs (inc idx) p1 base) (first input)) (+ idx 2) (drop 1 input) output base)
      4 (recur xs (+ idx 2) input (conj output (get-param-result xs (inc idx) p1 base)) base)
      5 (let [a (get-param-result xs (inc idx) p1 base)
              jump (not (zero? a))]
          (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) input output base))
      6 (let [a (get-param-result xs (inc idx) p1 base)
              jump (zero? a)]
          (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) input output base))
      7 (let [v (value <
                       (get-param-result xs (inc idx) p1 base)
                       (get-param-result xs (+ idx 2) p2 base))]
          (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) input output base))
      8 (let [v (value =
                       (get-param-result xs (inc idx) p1 base)
                       (get-param-result xs (+ idx 2) p2 base))]
          (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) input output base))
      9 (let [base2 (+ base (get-param-result xs (inc idx) p1 base))]
          (recur xs (+ idx 2) input output base2))))))

(println (intcode (pair-with-index cleaned-data) 0 [1] [] 0))
(println (intcode (pair-with-index cleaned-data) 0 [2] [] 0))
