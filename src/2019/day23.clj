(ns advent2019.day23
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.core.async
    :as async
    :refer [>!! <!! >! <! go-loop chan sliding-buffer]]))

(def data
  (->> "2019/day23"
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

(def nat-chan (chan (sliding-buffer 2)))
(def idle-chan (chan 50))
(def stop-chan (chan 1))

(def chans
  (for [_ (range 50)] (chan 100)))

(def kill-signal "KILL")

(defn intcode [xs-start
               idx-start
               base-start
               input-chan
               output-start]
(go-loop [xs xs-start
         idx idx-start
         base base-start
         output output-start]
    (let [parameters (quot (get-default-zero xs idx) 100) [p1 p2 p3] (parse-params parameters)]
      (case (mod (get-default-zero xs idx) 100)
        99 output
        1 (let [r (get-result xs idx + parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base output))
        2 (let [r (get-result xs idx * parameters base)]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) r) (+ idx 4) base output))
        3 (let [input (<! input-chan)]
            (case input
              kill-signal 0
              -1 (do
                   (>! idle-chan 1)
                   (recur (assoc xs (get-param-location xs (inc idx) p1 base) input) (+ idx 2) base output))
              (recur (assoc xs (get-param-location xs (inc idx) p1 base) input) (+ idx 2) base output)))
        4 (let [new-output (conj output (get-param-result xs (inc idx) p1 base))]
            (if (= (count new-output) 3)
              (let [[addr x y] new-output
                    send-chan (if (= addr 255) nat-chan (nth chans addr))]
                (>! send-chan x)
                (>! send-chan y)
                (recur xs (+ idx 2) base []))
              (recur xs (+ idx 2) base new-output)))
        5 (let [a (get-param-result xs (inc idx) p1 base)
                jump (not (zero? a))]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base output))
        6 (let [a (get-param-result xs (inc idx) p1 base)
                jump (zero? a)]
            (recur xs (if jump (get-param-result xs (+ idx 2) p2 base) (+ idx 3)) base output))
        7 (let [v (value <
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base output))
        8 (let [v (value =
                         (get-param-result xs (inc idx) p1 base)
                         (get-param-result xs (+ idx 2) p2 base))]
            (recur (assoc xs (get-param-location xs (+ idx 3) p3 base) v) (+ idx 4) base output))
        9 (let [base2 (+ base (get-param-result xs (inc idx) p1 base))]
            (recur xs (+ idx 2) base2 output))))))

(def start-memory
  (pair-with-index cleaned-data))

(defn add-input []
  (loop [idx 0]
    (if (= idx 50) 0
        (do (>!! (nth chans idx) idx)
            (recur (inc idx))))))
(add-input)

(defn add-1s []
  (loop [idx 0]
    (if (= idx 50) 0
        (do (>!! (nth chans idx) -1)
            (recur (inc idx))))))
(add-1s)

(defn start-machines []
  (loop [idx 0]
    (if (= idx 50) 0
        (do
          (intcode start-memory 0 0 (nth chans idx) [])
          (recur (inc idx))))))
(start-machines)

(defn nat-controller []
  (go-loop [num-idle 0]
    (if (= num-idle 50)
      (let [x (<! nat-chan)
            y (<! nat-chan)]
        (println [x y])
        (>! (first chans) x)
        (>! (first chans) y)
        (>! stop-chan y)
        (add-1s)
        (recur 0))
      (recur (+ num-idle (<! idle-chan))))))

(nat-controller)

(defn kill-machines []
  (loop [idx 0]
    (if (= idx 50) 0
        (do (>!! (nth chans idx) kill-signal)
            (recur (inc idx))))))

(def part2-chan (chan 1))

(defn stop-controller []
  (go-loop [curr-y 0]
    (let [next-y (<! stop-chan)]
      (if (= next-y curr-y)
        (do
          (kill-machines)
          (>! part2-chan next-y))
        (recur next-y)))))

(stop-controller)

(println (<!! part2-chan))