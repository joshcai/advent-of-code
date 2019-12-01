(ns advent2019.day01
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day01"
       io/resource
       io/reader
       line-seq
       (map #(Integer/parseInt %))))

(defn fuel-needed [x] (- (quot x 3) 2))

(def part1 
  (reduce + (map fuel-needed data)))

(println part1)

(defn fuel-recurse [x]
  (if (< x 6)
    0
    (let [current (fuel-needed x)]
         (+ current (fuel-recurse current)))))

(def part2
  (reduce + (map fuel-recurse data)))

(println part2)