(ns advent2019.day07
  (:require
   [clojure.math.combinatorics :as combo]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.core.async
    :as async
    :refer [>! <! >!! <!! go chan buffer close! thread put!
            alts! alts!! timeout]]))

(def data
  (->> "2019/day07"
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

(defn value [op a b]
  (if (op a b) 1 0))

(defn part1 [xs idx input output]
  (let [parameters (quot (nth xs idx) 100) [p1 p2] (parse-params parameters)]
    (case (mod (nth xs idx) 100)
      99 output
      1 (let [r (get-result xs idx + parameters)]
          (part1 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4) input output))
      2 (let [r (get-result xs idx * parameters)]
          (part1 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4) input output))
      3 (part1 (assoc xs (nth xs (inc idx)) (first input)) (+ idx 2) (drop 1 input) output)
      4 (part1 xs (+ idx 2) input (get-param-result xs (inc idx) p1))
      5 (let [a (get-param-result xs (inc idx) p1)
              jump (not (zero? a))]
          (part1 xs (if jump (get-param-result xs (+ idx 2) p2) (+ idx 3)) input output))
      6 (let [a (get-param-result xs (inc idx) p1)
              jump (zero? a)]
          (part1 xs (if jump (get-param-result xs (+ idx 2) p2) (+ idx 3)) input output))
      7 (let [v (value <
                       (get-param-result xs (inc idx) p1)
                       (get-param-result xs (+ idx 2) p2))]
          (part1 (assoc xs (nth xs (+ idx 3)) v) (+ idx 4) input output))
      8 (let [v (value =
                       (get-param-result xs (inc idx) p1)
                       (get-param-result xs (+ idx 2) p2))]
          (part1 (assoc xs (nth xs (+ idx 3)) v) (+ idx 4) input output)))))

(def perms (combo/permutations [0 1 2 3 4]))

(defn run-amplifiers [perm]
  (reduce (fn [input amp] (part1 cleaned-data 0 [amp input] 0)) 0 perm))

(def max-amp 
  (apply max (map run-amplifiers perms)))
(println max-amp)

; input / output are now channels
(defn part2 [xs idx input output done]
  (let [parameters (quot (nth xs idx) 100) [p1 p2] (parse-params parameters)]
    (case (mod (nth xs idx) 100)
      99 (>!! done 5)
      1 (let [r (get-result xs idx + parameters)]
          (part2 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4) input output done))
      2 (let [r (get-result xs idx * parameters)]
          (part2 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4) input output done))
      3 (part2 (assoc xs (nth xs (inc idx)) (<!! input)) (+ idx 2) input output done)
      4 (do (>!! output (get-param-result xs (inc idx) p1))
            (part2 xs (+ idx 2) input output done))
      5 (let [a (get-param-result xs (inc idx) p1)
              jump (not (zero? a))]
          (part2 xs (if jump (get-param-result xs (+ idx 2) p2) (+ idx 3)) input output done))
      6 (let [a (get-param-result xs (inc idx) p1)
              jump (zero? a)]
          (part2 xs (if jump (get-param-result xs (+ idx 2) p2) (+ idx 3)) input output done))
      7 (let [v (value <
                       (get-param-result xs (inc idx) p1)
                       (get-param-result xs (+ idx 2) p2))]
          (part2 (assoc xs (nth xs (+ idx 3)) v) (+ idx 4) input output done))
      8 (let [v (value =
                       (get-param-result xs (inc idx) p1)
                       (get-param-result xs (+ idx 2) p2))]
          (part2 (assoc xs (nth xs (+ idx 3)) v) (+ idx 4) input output done)))))

(def perms2 (combo/permutations [5 6 7 8 9]))

(defn run-amplifiers2 [perm]
  (let [
        a (chan 10) 
        b (chan 10) 
        c (chan 10) 
        d (chan 10) 
        e (chan 10) 
        dummy (chan 10) 
        done (chan 10) 
        [anum bnum cnum dnum enum] perm]

    (go (part2 cleaned-data 0 a b dummy))
    (go (part2 cleaned-data 0 b c dummy))
    (go (part2 cleaned-data 0 c d dummy))
    (go (part2 cleaned-data 0 d e dummy))
    (go (part2 cleaned-data 0 e a done))
    (>!! a anum)
    (>!! b bnum)
    (>!! c cnum)
    (>!! d dnum)
    (>!! e enum)
    (>!! a 0)
    (<!! done)
    (<!! a)))

(def max-amp2
  (apply max (map run-amplifiers2 perms2)))
(println max-amp2)
