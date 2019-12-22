(ns advent2019.day16
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day16"
       io/resource
       io/reader
       line-seq))

(def cleaned-data
  (map #(Integer/parseInt (str %)) (seq (first data))))

(def pattern 
  [0 1 0 -1])

(defn get-num [output-idx idx]
  (nth pattern (mod (int (/ (inc idx) (inc output-idx))) 4)))

(defn get-nums [output-idx]
  ((fn generator
    ([] (generator 0))
    ([n] (lazy-seq (cons (get-num output-idx n) (generator (inc n))))))))

(defn abs [x]
  (if (pos? x) x (* -1 x)))

(defn get-output-digit [digits output-idx]
  (mod (abs
        (reduce + (map * digits (get-nums output-idx)))) 10))

(defn step [xs]
  (map #(get-output-digit xs %) (range 0 (count xs))))

(defn step-n [xs n]
  (if (zero? n) 
    xs
    (recur (step xs) (dec n))))

(println (apply str (take 8 (step-n cleaned-data 100))))

(def total-count 
  (* (count cleaned-data) 10000))

(def start 5970221)

(def actual-count
  (- total-count start))

(def reversed-data
  (reverse cleaned-data))

(defn reversed-data-gen
    ([] (reversed-data-gen 0))
    ([n] (lazy-seq (cons (nth reversed-data n) 
                         (reversed-data-gen (mod (inc n) (count cleaned-data)))))))

(def reversed-start
  (take actual-count (reversed-data-gen)))

(defn step2 [data]
  (drop 1 (reductions (fn [a b] (mod (+ a b) 10)) 0 data)))

(defn step2-n [xs n]
  (if (zero? n) 
    xs
    (recur (step2 xs) (dec n))))

(println (apply str (take 8 (reverse (step2-n reversed-start 100)))))
