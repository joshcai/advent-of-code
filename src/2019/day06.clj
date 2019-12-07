(ns advent2019.day06
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day06"
       io/resource
       io/reader
       line-seq
       (map #(clojure.string/split % #"\)"))))

(def graph 
  (reduce 
   (fn [coll x]
     (assoc coll (first x)
            (conj (get coll (first x) []) (last x))))
     {} data))

(defn orbits [x depth]
  (let [children (get graph x nil)]
    (if (nil? children) depth
        (+ depth (apply + (map #(orbits % (inc depth)) children))))))

(println (orbits "COM" 0))

(defn traversal [current target1 target2]
(if (= current target1) [1 0]
    (if (= current target2) [0 1]
        (if (nil? (get graph current nil)) [0 0]
            (let [[x y] (reduce (fn [a b] (map + a b))
                             (map #(traversal % target1 target2)
                                  (get graph current)))]
              (if (= (= x 0) (= y 0)) [x y]
                  (if (= x 0) [x (inc y)] [(inc x) y])))))))

(def weights (traversal "COM" "SAN" "YOU"))

(println (- (apply + weights) 2))