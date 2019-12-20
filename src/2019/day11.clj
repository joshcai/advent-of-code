(ns advent2019.day11
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day11"
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

(defn turn-direction [curr new]
  (if (zero? new)
    (case curr
      :up :left
      :down :right
      :right :up
      :left :down)
     (case curr
       :up :right
       :down :left
       :right :down
       :left :up)))

(defn move [x y dir]
  (case dir 
    :up [x (inc y)]
    :down [x (dec y)]
    :left [(dec x) y]
    :right [(inc x) y]))

(defn intcode [xs-start 
               idx-start 
               base-start
               x-start ; x of the robot
               y-start ; y of the robot
               dir-start ; current direction of the robot
               paint-start ; bool on whether next output is paint color or dir
               painted-tiles-start ; map of tiles that have been painted with color
               ]
  (loop [xs xs-start
         idx idx-start
         base base-start
         x x-start
         y y-start
         dir dir-start
         paint paint-start
         painted-tiles painted-tiles-start]
    (let [parameters (quot (get-default-zero xs idx) 100) [p1 p2 p3] (parse-params parameters)]
      (case (mod (get-default-zero xs idx) 100)
        99 painted-tiles
        1 (let [r (get-result xs idx + parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base x y dir paint painted-tiles))
        2 (let [r (get-result xs idx * parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base x y dir paint painted-tiles))
        3 (recur (assoc xs (get-param-location xs (inc idx) p1 base) (get-color painted-tiles x y)) (+ idx 2) base x y dir paint painted-tiles)
        4 (let [r (get-param-result xs (inc idx) p1 base)]
            (if (true? paint)
              (recur xs (+ idx 2) base x y dir (not paint) (update-painted-tiles painted-tiles x y r))
              (let [new-dir (turn-direction dir r)
                    [newx newy] (move x y new-dir)]
                (recur xs (+ idx 2) base newx newy new-dir (not paint) painted-tiles)))
            )
        5 (let [a (get-param-result xs (inc idx) p1 base)
                jump (not (zero? a))]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base x y dir paint painted-tiles))
        6 (let [a (get-param-result xs (inc idx) p1 base)
                jump (zero? a)]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base x y dir paint painted-tiles))
        7 (let [v (value <
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base x y dir paint painted-tiles))
        8 (let [v (value =
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base x y dir paint painted-tiles))
        9 (let [base2 (+ base (get-param-result xs (inc idx) p1 base))]
            (recur xs (+ idx 2) base2 x y dir paint painted-tiles))))))

(println (count (intcode (pair-with-index cleaned-data) 0 0 0 0 :up true {})))

(def painted-tiles
  (intcode (pair-with-index cleaned-data) 0 0 0 0 :up true {[0 0] 1}))

(def painted-tiles-x 
  (map #(first (first %)) painted-tiles))

(def painted-tiles-y
  (map #(second (first %)) painted-tiles))

(defn get-pretty-color [painted-tiles x y]
  (if (zero? (get-color painted-tiles x y)) "." "0"))

(for [y (range (apply max painted-tiles-y) (dec (apply min painted-tiles-y)) -1)]
  (println (apply str
                  (map #(get-pretty-color painted-tiles % y) 
                       (range (apply min painted-tiles-x) (inc (apply max painted-tiles-x)))))))