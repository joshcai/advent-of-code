(ns advent2019.day02
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day02"
       io/resource
       io/reader
       line-seq))

(def cleaned-data 
  (into [] (map #(Integer/parseInt %) (str/split (first data) #","))))

(defn modify-data [xs noun verb]
   (assoc (assoc xs 1 noun) 2 verb))

(def modified-data (modify-data cleaned-data 12 2))

(defn get-result [xs idx op]
  (let [a (nth xs (nth xs (+ idx 1))) b (nth xs (nth xs (+ idx 2)))]
    (op a b)))

(defn part1 [xs idx]
  (case (nth xs idx)
    99 xs
    1 (let [r (get-result xs idx +)]
      (part1 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4)))
    2 (let [r (get-result xs idx *)]
      (part1 (assoc xs (nth xs (+ idx 3)) r) (+ idx 4)))
  ))

(defn get-sol [xs]
  (first (part1 xs 0)))

(println (get-sol modified-data))

(def part2 
  (take 1 (for [noun (range 100)
                verb (range 100)
                :when (= 19690720 (get-sol (modify-data cleaned-data noun verb)))]
        [noun verb])))

(println part2)