(ns advent2019.day10
  (:require
   [clojure.algo.generic.functor :as functor]
   [clojure.algo.generic.math-functions :as math]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day10"
       io/resource
       io/reader
       line-seq))

(defn pair-with-index [xs]
  (zipmap (iterate inc 0) xs))

(def cleaned-data
  (pair-with-index
   (map #(pair-with-index (str/split % #"")) data)))

(defn filter-asteroids [line]
   (map #(first %) (filter (fn [pair] (= (last pair) "#")) line)))

(def asteroids 
  (apply concat 
         (reduce (fn [coll line] 
                   (conj coll 
                         (into [] (zipmap 
                                   (filter-asteroids (last line)) 
                                   (repeat (first line)))))) 
                 [] cleaned-data)))


(defn count-asteroids [asteroids curr]
  (count (into #{} (map (fn [coord] 
         (let [[x y] (map - coord curr)] (math/atan2 y x)))
       (filter #(not= curr %) asteroids)))))

(def part1 
 (apply max (map #(count-asteroids asteroids %) asteroids)))
(println part1)

(def max-coords 
  (reduce (fn [x y] (if (> (first x) (first y)) x y)) [0 [0 0]]
  (map (fn [curr] [(count-asteroids asteroids curr) curr]) asteroids)))
(println max-coords)

(defn distance [x y] 
  (math/sqrt (+ (* x x) (* y y))))

(def max-asteroid [17 22])

(def asteroids2 
  (filter #(not= max-asteroid %) asteroids))

(defn shift [rad] 
  (if (pos? rad) 
    (if (<= rad (/ Math/PI 2))
      (- (/ Math/PI 2) rad)
      (+ (- Math/PI rad) Math/PI (/ Math/PI 2)))
    (+ (* -1 rad) (/ Math/PI 2))
    ))

(def transformed-asteroids
  (functor/fmap #(sort-by :dist %)
                (group-by :deg (map (fn [coord] 
                                      (let [[x y] (map - coord max-asteroid)]
                                        {:deg (shift (math/atan2 (* -1 y) x))
                                         :dist (distance x y)
                                         :coords coord
                                         })) asteroids2))))
(println (count transformed-asteroids))

(def max-count
  (apply max (map #(count (last %)) transformed-asteroids)))

(def sorted-asteroids
  (apply interleave
         (map (fn [pair] (let [[_ v] pair]
                           (concat v (repeat (- max-count (count v)) nil))))
              (into (sorted-map-by <) transformed-asteroids))))

(def filtered-asteroids
  (filter #(not= % nil) sorted-asteroids))

(def part2
  (:coords (nth filtered-asteroids 199)))

(println part2)

