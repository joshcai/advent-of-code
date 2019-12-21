(ns advent2019.day12
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day12"
       io/resource
       io/reader
       line-seq))

(defn parse-x [s]
  (Long/parseLong (last (re-matches #"<x=(-?\d+),.*" s))))

(defn parse-y [s]
  (Long/parseLong (last (re-matches #".*y=(-?\d+),.*" s))))

(defn parse-z [s]
  (Long/parseLong (last (re-matches #".*z=(-?\d+)>" s))))

(defn pair-with-index [xs]
  (zipmap (iterate inc 0) xs))

(def cleaned-data
  (map (fn [line] 
         {:pos 
          {:x (parse-x line)
           :y (parse-y line)
           :z (parse-z line)}
          :vel {:x 0 :y 0 :z 0}}) data))

(defn calc-vel [data curr]
  (reduce (fn [a b] {:x (+ (:x a) (:x b))
                     :y (+ (:y a) (:y b))
                     :z (+ (:z a) (:z b))})
          (:vel curr)
          (map (fn [other] {:x (compare (:x (:pos other)) (:x (:pos curr)))
                            :y (compare (:y (:pos other)) (:y (:pos curr)))
                            :z (compare (:z (:pos other)) (:z (:pos curr)))}) data)))

(defn calc-pos [curr]
  {:x (+ (:x (:pos curr)) (:x (:vel curr)))
   :y (+ (:y (:pos curr)) (:y (:vel curr)))
   :z (+ (:z (:pos curr)) (:z (:vel curr)))})

(defn abs [x]
  (if (pos? x) x (* -1 x)))

(defn calc-energy-partial [coords]
  (+ (abs (:x coords)) (abs (:y coords)) (abs (:z coords))))

(defn calc-energy [curr]
  (* (calc-energy-partial (:pos curr)) (calc-energy-partial (:vel curr))))

(defn calc-energy-total [data]
  (reduce + (map calc-energy data)))

(defn step [data]
  (map (fn [curr] (assoc curr :pos (calc-pos curr)))
       (map (fn [curr] (assoc curr :vel (calc-vel data curr))) data)))

(defn step-n [data n]
  (if (zero? n) data
      (recur (doall (step data)) (dec n))))

(println (calc-energy-total (step-n cleaned-data 1000)))

(defn get-axis-vector [data axis]
  (concat
   (map (fn [curr] (axis (:pos curr))) data)
   (map (fn [curr] (axis (:vel curr))) data)))

(defn num-cycles [data axis n seen]
  (let [vec (get-axis-vector data axis)]
    (if (contains? seen vec) n
        (recur (doall (step data)) axis (inc n) (conj seen vec)))))

(def x-cycles (num-cycles cleaned-data :x 0 #{}))
(def y-cycles (num-cycles cleaned-data :y 0 #{}))
(def z-cycles (num-cycles cleaned-data :z 0 #{}))

(defn gcd [a b]
  (if (zero? b) a
    (recur b (mod a b))))

(defn lcm [a b]
  (/ (* a b) (gcd a b)))

(println (lcm (lcm x-cycles y-cycles) z-cycles))