(ns advent2019.day22
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day22"
       io/resource
       io/reader
       line-seq))

(def start-deck (range 10007))

(defn parse-line [line]
  (let [m1 (last (re-matches #"deal with increment (\d+)" line))]
    (if (not (nil? m1)) 
      {:type "DEAL" :num (Integer/parseInt m1)}
      (let [m2 (last (re-matches #"cut (-?\d+)" line))]
        (if (not (nil? m2)) 
          {:type "CUT" :num (Integer/parseInt m2)}
          {:type "REVERSE"}
          )))))

(defn pair-with-index [xs]
  (zipmap (iterate inc 0) xs))

(defn deal [xs n]
  (let [total (count xs)
        mapped (pair-with-index xs)]
    (reduce (fn [coll i]
              (let [num (get mapped i)]
                (assoc coll (mod (* i n) total) num))) (into (vector) xs) (range total))))

(defn cut [xs n]
  (let [cut-n (if (neg? n) (+ (count xs) n) n)]
    (apply concat (reverse (split-at cut-n xs)))))

(defn step [xs transform]
  (let [t (:type transform)
        n (:num transform)]
    (case t
      "DEAL" (deal xs n)
      "CUT" (cut xs n)
      "REVERSE" (reverse xs))))

(def transformed-cards 
  (reduce (fn [coll line]
            (step coll (parse-line line)))
          start-deck
          data))

(defn pair-with-index-reversed [xs]
  (zipmap xs (iterate inc 0)))

(println (get (pair-with-index-reversed transformed-cards) 2019))

;; Part 2 solved with refernece from:
;; https://www.reddit.com/r/adventofcode/comments/ee0rqi/2019_day_22_solutions/fbnifwk/?st=k4kwhy4x&sh=7dd794ef
(defn mod-pow [base exp m]
  (.modPow (biginteger base) (biginteger exp) (biginteger m)))

(defn mod-inverse [a b]
  (.modInverse (biginteger a) (biginteger b)))

(defn deal-pos [idx total n]
  (mod (* (mod-inverse n total) idx) total))

(defn cut-pos [idx total n]
  (let [cut-n (if (neg? n) (+ total n) n)]
    (mod (+ idx cut-n) total)))

(defn reverse-pos [idx total]
  (dec (- total idx)))

(defn step-pos [idx total transform]
  (let [t (:type transform)
        n (:num transform)]
    (case t
      "DEAL" (deal-pos idx total n)
      "CUT" (cut-pos idx total n)
      "REVERSE" (reverse-pos idx total))))

(defn pos-step [idx total]
  (reduce (fn [start-idx line]
            (let [next (step-pos start-idx total (parse-line line))]
              next
              ))
          idx
          (reverse data)))

(def part2-total 119315717514047)
(def part2-times 101741582076661)
(def part2-start-idx 2020)
(def step1 (pos-step part2-start-idx part2-total))
(def step2 (pos-step step1 part2-total))
(def a (mod (* (- step1 step2)
               (mod-inverse (+ (- part2-start-idx step1) part2-total) part2-total)) part2-total))
(def b (mod (- step1 (* a part2-start-idx)) part2-total))

(def part2
  (+
   (* (dec (mod-pow a part2-times part2-total)) (mod-inverse (dec a) part2-total) b)
   (* (mod-pow a part2-times part2-total) part2-start-idx)))

(println (mod part2 part2-total))