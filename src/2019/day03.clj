(ns advent2019.day03
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day03"
       io/resource
       io/reader
       line-seq))

(def num-pattern (re-pattern "\\d+"))
(def char-pattern (re-pattern "\\w"))

(defn clean-data [wire] 
  (map 
   (fn [x] {:dir (re-find char-pattern x), 
      :len (Integer/parseInt (re-find num-pattern x))})
   (str/split wire #",")))

(def first-wire (clean-data (first data)))
(def second-wire (clean-data (last data)))

(defn update-set [s x y nx ny]
  (reduce conj s
  (for [currx (range x (inc nx) (if (< nx x) -1 1))
        curry (range y (inc ny) (if (< ny y) -1 1))] [currx curry]))
  )

(defn build-set [x y wire idx s]
  (let [curr (nth wire idx nil)]
    (if (nil? curr) s
        (case (:dir curr)
          "R" (build-set (+ x (:len curr)) y wire (inc idx)
              (update-set s x y (+ x (:len curr)) y))
          "L" (build-set (- x (:len curr)) y wire (inc idx) 
              (update-set s x y (- x (:len curr)) y))
          "U" (build-set x (+ y (:len curr)) wire (inc idx) 
              (update-set s x y x (+ y (:len curr))))
          "D" (build-set x (- y (:len curr)) wire (inc idx) 
              (update-set s x y x (- y (:len curr))))
          )))
  )

(def first-set (build-set 0 0 first-wire 0 #{}))
(def second-set (build-set 0 0 second-wire 0 #{}))

;; Remove [0 0] since we don't count the origin as an intersection.
(def intersect-set (disj (clojure.set/intersection first-set second-set) [0 0]))

(defn abs [n] (max n (- n)))

(def part1 (apply min (map #(+ (abs (first %)) (abs (last %))) intersect-set)))
(println part1)

(defn update-map [total s m x y nx ny]
  (reduce 
   (fn [coll n]
     ;; If the intersection already has an entry in the map, keep it, since
     ;; we care about the first seen when an intersection is visited twice.
     (if (contains? coll n) coll
         (assoc coll n (+ total 
                        (abs (- (first n) x))
                        (abs (- (last n) y))))))
   m
   (for [currx (range x (inc nx) (if (< nx x) -1 1))
         curry (range y (inc ny) (if (< ny y) -1 1))
         :when (contains? s [currx curry])] [currx curry]))
  )

(defn build-map [x y wire idx s total m]
  (let [curr (nth wire idx nil)]
    (if (nil? curr) m
        (let [l (:len curr)]
          (case (:dir curr)
            "R" (build-map (+ x l) y wire (inc idx) s (+ total l)
                           (update-map total s m x y (+ x l) y))
            "L" (build-map (- x l) y wire (inc idx) s (+ total l)
                           (update-map total s m x y (- x l) y))
            "U" (build-map x (+ y l) wire (inc idx) s (+ total l)
                           (update-map total s m x y x (+ y l)))
            "D" (build-map x (- y l) wire (inc idx) s (+ total l)
                           (update-map total s m x y x (- y l))))))))

(println (coll? intersect-set))

(def first-map (build-map 0 0 first-wire 0 intersect-set 0 {}))
(def second-map (build-map 0 0 second-wire 0 intersect-set 0 {}))

(def part2 (apply min (map #(+ (first-map %) (second-map %)) intersect-set)))
(println part2)