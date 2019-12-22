(ns advent2019.day15
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def data
  (->> "2019/day15"
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
        99 [xs idx base output]
        1 (let [r (get-result xs idx + parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base input output))
        2 (let [r (get-result xs idx * parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base input output))
        3 (recur (assoc xs (get-param-location xs (inc idx) p1 base) input) (+ idx 2) base input output)
        4 [xs (+ idx 2) base (get-param-result xs (inc idx) p1 base)]
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
            (recur xs (+ idx 2) base2 input output ))))))


(def directions [1 2 3 4])
(def direction-map 
  {1 [0 1]
   2 [0 -1]
   3 [-1 0]
   4 [1 0]})

(defn generate-nodes [curr seen]
  (let [coords (:coords curr)
        dist (:dist curr)]
    (filter (fn [node] (and 
                        (not (contains? seen (:coords node))) 
                        (not= (:output node) 0)))
            (map (fn [dir]
                   (let [[mem idx base output] 
                         (intcode (:mem curr) (:idx curr) (:base curr) dir 0)]
                     {:coords (map + coords (get direction-map dir))
                      :dist (inc dist)
                      :mem mem
                      :idx idx
                      :base base
                      :output output})) directions))))

(defn bfs [memory idx base]
  (loop [queue [{:coords [0 0]
                 :dist 0
                 :mem memory
                 :idx idx
                 :base base
                 :output 1}]
         seen #{[0 0]}]
    (when (seq queue)
      (let [[node & other-nodes-temp] queue]
        (if (= (:output node) 2)
          node
          (let [other-nodes (into (vector) other-nodes-temp)
                new-queue (reduce conj other-nodes (generate-nodes node seen))
                new-seen (reduce conj seen (map #(map + (:coords node) (get direction-map %)) directions))]
            (recur new-queue new-seen)))))))

(def start-memory
  (pair-with-index cleaned-data))

(def oxygen-location 
  (bfs start-memory 0 0))

(println (:dist oxygen-location))

(defn bfs2 [memory idx base]
  (loop [queue [{:coords [0 0]
                 :dist 0
                 :mem memory
                 :idx idx
                 :base base
                 :output 1}]
         seen #{[0 0]}
         current-max 0]
    (if (seq queue)
      (let [[node & other-nodes-temp] queue
            other-nodes (into (vector) other-nodes-temp)
            new-queue (reduce conj other-nodes (generate-nodes node seen))
            new-seen (reduce conj seen (map #(map + (:coords node) (get direction-map %)) directions))]
            (recur new-queue new-seen (max current-max (:dist node))))
    current-max)
  ))

(println (bfs2 (:mem oxygen-location) (:idx oxygen-location) (:base oxygen-location)))