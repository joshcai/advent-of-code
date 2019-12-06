(ns advent2019.day05
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day05"
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
    [(first xs) (second xs)]))

(defn get-param-result [xs idx p]
  (if (zero? p)
    (nth xs (nth xs idx))
    (nth xs idx)))

(defn get-result [xs idx op parameters]
  (let 
   [[p1 p2] (parse-params parameters)
    a (get-param-result xs (+ idx 1) p1)
    b (get-param-result xs (+ idx 2) p2)]
    (op a b)))

(def input 1)

(defn part1 [xs idx]
(let [parameters (quot (nth xs idx) 100)]
  (case (mod (nth xs idx) 100)
    99 xs
    1 (let [r (get-result xs idx + parameters)]
        (part1 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4)))
    2 (let [r (get-result xs idx * parameters)]
        (part1 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4)))
    3 (part1 (assoc xs (nth xs (inc idx)) input) (+ idx 2))
    4 (do 
        (let [[p _] (parse-params parameters)]
        (println (get-param-result xs (inc idx) p)))
        (part1 xs (+ idx 2)))
    )))

(part1 cleaned-data 0)


(defn value [op a b]
  (if (op a b) 1 0))

(def input-part2 5)

(defn part2 [xs idx]
(let [parameters (quot (nth xs idx) 100) [p1 p2] (parse-params parameters)]
  (case (mod (nth xs idx) 100)
    99 xs
    1 (let [r (get-result xs idx + parameters)]
        (part2 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4)))
    2 (let [r (get-result xs idx * parameters)]
        (part2 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4)))
    3 (part2 (assoc xs (nth xs (inc idx)) input-part2) (+ idx 2))
    4 (do
        (println (get-param-result xs (inc idx) p1))
        (part2 xs (+ idx 2)))
    5 (let [a (get-param-result xs (inc idx) p1)
            jump (not (zero? a))]
        (part2 xs (if jump (get-param-result xs (+ idx 2) p2) (+ idx 3))))
    6 (let [a (get-param-result xs (inc idx) p1)
            jump (zero? a)]
        (part2 xs (if jump (get-param-result xs (+ idx 2) p2) (+ idx 3))))
    7 (let [v (value <
                   (get-param-result xs (inc idx) p1)
                   (get-param-result xs (+ idx 2) p2))]
      (part2 (assoc xs (nth xs (+ idx 3)) v) (+ idx 4)))
    8 (let [v (value =
                   (get-param-result xs (inc idx) p1)
                   (get-param-result xs (+ idx 2) p2))]
      (part2 (assoc xs (nth xs (+ idx 3)) v) (+ idx 4))))))

(part2 cleaned-data 0)