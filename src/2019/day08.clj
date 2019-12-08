(ns advent2019.day08
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day08"
       io/resource
       io/reader
       line-seq))

(def cleaned-data
  (into [] (map #(Integer/parseInt %) (str/split (first data) #""))))

(def image-size (* 25 6))

(def mapped (map frequencies (partition image-size cleaned-data)))

(def min-layer (apply min-key #(get % 0) mapped))

(println (* (get min-layer 1) (get min-layer 2)))

(def combined (apply map 
                     (fn [& xs] (reduce 
                                 (fn [x y] (if (= x 2) y x)) xs))
                     (partition image-size cleaned-data)))

; Convert 0's to _'s to make it easier to read the final "image".
(defn display-image []
  (doseq [line (partition 25 combined)]
    (println (map (fn [x] (if (= 0 x) "_" x)) line))))

(display-image)