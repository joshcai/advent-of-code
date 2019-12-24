(ns advent2019.day21
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day21"
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

(defn intcode [xs-start
               idx-start
               base-start
               input-start
               output-start]
  (loop [xs xs-start
         idx idx-start
         base base-start
         input input-start
         output output-start]
    (let [parameters (quot (get-default-zero xs idx) 100) [p1 p2 p3] (parse-params parameters)]
      (case (mod (get-default-zero xs idx) 100)
        99 output
        1 (let [r (get-result xs idx + parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base input output))
        2 (let [r (get-result xs idx * parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base input output))
        3 (recur (assoc xs (get-param-location xs (inc idx) p1 base) (first input)) (+ idx 2) base (drop 1 input) output)
        4 (recur xs (+ idx 2) base input (conj output (get-param-result xs (inc idx) p1 base)))
        5 (let [a (get-param-result xs (inc idx) p1 base)
                jump (not (zero? a))]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base input output))
        6 (let [a (get-param-result xs (inc idx) p1 base)
                jump (zero? a)]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base input output))
        7 (let [v (value <
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base input output))
        8 (let [v (value =
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base input output))
        9 (let [base2 (+ base (get-param-result xs (inc idx) p1 base))]
            (recur xs (+ idx 2) base2 input output))))))

(def start-memory
  (pair-with-index cleaned-data))

(defn convert-to-strs [xs]
  (str/split 
   (apply str (map #(char %) (filter #(< % 1000) xs)))
   #"\n"))

(def part1-input
 (map #(int %)
      (seq (apply str
      (map #(str % "\n")
           ["NOT A T"
            "OR T J"
            "NOT B T"
            "OR T J"
            "NOT C T"
            "OR T J"
            "AND D J"
            "WALK"])))))

(def part1
  (intcode start-memory 0 0 part1-input []))
(println (convert-to-strs part1))
(println (last part1))

(def part2-input
 (map #(int %)
      (seq (apply str
      (map #(str % "\n")
           ["NOT A T"
            "OR T J"
            "NOT B T"
            "OR T J"
            "NOT C T"
            "OR T J"
            "NOT E T"
            "NOT T T"
            "OR H T"
            "AND T J"
            "AND D J"
            "RUN"])))))

(def part2
  (intcode start-memory 0 0 part2-input []))
(println (convert-to-strs part2))
(println (last part2))

