(ns advent2019.day19
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day19"
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
        4 (recur xs (+ idx 2) base input (get-param-result xs (inc idx) p1 base))
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

(defn beam? [x y]
  (= 1 (intcode start-memory 0 0 [x y] -1)))

(defn part1 [n]
  (count (filter #(true? %)
                 (for [x (range n)
                       y (range n)]
                   (beam? x y)))))
  
(println (part1 50))

(defn beam-set-picture []
  (for [y (range 100)]
    (apply str 
           (map (fn [digit]
                     (if (zero? digit) "." "#"))
                   (for [x (range 100)]
                     (intcode start-memory 0 0 [x y] -1))))))


(defn valid-point? [coords]
  (let [[x y] coords]
    (and
     (beam? (+ x 99) y)
     (beam? x (+ y 99))
     (beam? (+ x 99) (+ y 99))
     (beam? x y))))

(defn bin-search [min-start max-start compare-fn]
  (loop [min min-start
         max max-start]
    (let [curr (long (/ (+ max min) 2))]
      (case (compare-fn curr)
        -1 (recur curr max)
        0 curr
        1 (recur min curr)))))

(defn get-beam-range-x [y]
  (let [lower-compare
        (fn [x]
          (let [b1 (beam? (dec x) y)
                b2 (beam? x y)]
            (if (= b1 b2 true) 1
                (if (= b1 b2 false) -1 0))))
        upper-compare
        (fn [x]
          (let [b1 (beam? x y)
                b2 (beam? (inc x) y)]
            (if (= b1 b2 true) -1
                (if (= b1 b2 false) 1 0))))
        mid (int (* (/ 24 31) y))]
    [(bin-search 0 mid lower-compare)
     (bin-search mid (* 2 mid) upper-compare)]))

(defn get-beam-range-y [x]
  (let [lower-compare
        (fn [y]
          (let [b1 (beam? x (dec y))
                b2 (beam? x y)]
            (if (= b1 b2 true) 1
                (if (= b1 b2 false) -1 0))))
        upper-compare
        (fn [y]
          (let [b1 (beam? x y)
                b2 (beam? x (inc y))]
            (if (= b1 b2 true) -1
                (if (= b1 b2 false) 1 0))))
        mid (int (* (/ 31 24) x))]
    [(bin-search 0 mid lower-compare)
     (bin-search mid (* 2 mid) upper-compare)]))

(defn x-width [y]
  (apply - (reverse (get-beam-range-x y))))
(defn get-starting-beam-y []
  (bin-search 304 1000 (fn [curr] (compare (x-width curr) 100))))


(defn check-line-y [y]
  (let [[min max] (get-beam-range-x y)]
    (loop [x min]
      (if (> x max) nil
          (if (valid-point? [x y]) [x y]
              (recur (inc x)))))))

(defn check-line-x [x]
  (let [[min max] (get-beam-range-y x)]
    (loop [y min]
      (if (> y max) nil
          (if (valid-point? [x y]) [x y]
              (recur (inc y)))))))

(def part2
  (loop [y 1250]
    (let [r (check-line-y y)]
      (if (nil? r) (recur (inc y))
          r))))
(println part2)
