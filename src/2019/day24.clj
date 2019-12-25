(ns advent2019.day24
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day24"
       io/resource
       io/reader
       line-seq))

(defn pair-with-index [xs]
  (zipmap (iterate inc 0) xs))

(defn convert-line [line y]
  (map (fn [pair]
         (let [[x c] pair]
           [[x y] c]))
       (pair-with-index (seq line))))

(def initial-grid
  (reduce (fn [coll pair]
            (let [[y line] pair]
              (apply conj coll (convert-line line y)))) {} (pair-with-index data)))

(def not-nil? (complement nil?))

(def directions
  [[0 1]
   [0 -1]
   [-1 0]
   [1 0]])

(defn bugs-around [grid x y]
  (count (filter #(= % \#) (map #(get grid (map + % [x y])) directions))))

(defn transform [grid x y]
  (let [curr (get grid [x y])
        num-bugs (bugs-around grid x y)]
    (case curr
      \. (if (or (= num-bugs 1) (= num-bugs 2)) \# \.)
      \# (if (= num-bugs 1) \# \.))))

(defn step [grid]
  (reduce (fn [coll next]
            (let [[[x y] _] next
                  next-char (transform grid x y)]
              (assoc coll [x y] next-char))) grid grid))

(defn step-until-duplicate [initial-grid]
  (loop [grid initial-grid
         seen-grids #{}]
    (if (contains? seen-grids grid) 
      grid
      (recur (step grid) (conj seen-grids grid)))))

(def duplicate-grid
  (step-until-duplicate initial-grid))

(defn exp [x n]
  (loop [acc 1 n n]
    (if (zero? n) acc
        (recur (* x acc) (dec n)))))

(defn biodiversity-score [grid]
  (reduce +
          (map (fn [pair]
                 (let [[[x y] _] pair]
                   (exp 2
                        (+ (* (count (first (seq data))) y)
                           x))))
               (filter #(= (second %) \#) grid))))

(println (biodiversity-score duplicate-grid))

(def part2-initial-grid
  (assoc initial-grid [2 2] \space))

(def default-grid
  (assoc
   (reduce (fn [coll next] (assoc coll (first next) \.)) initial-grid initial-grid)
   [2 2] \space))

(def top-left [0 0])
(def top-right [4 0])
(def bottom-left [0 4])
(def bottom-right [4 4])

(def left-edge-mis #{[0 1] [0 2] [0 3]})
(def right-edge-mis #{[4 1] [4 2] [4 3]})
(def upper-edge-mis #{[1 0] [2 0] [3 0]})
(def lower-edge-mis #{[1 4] [2 4] [3 4]})

(def left-edge #{[0 0] [0 1] [0 2] [0 3] [0 4]})
(def right-edge #{[4 0] [4 1] [4 2] [4 3] [4 4]})
(def upper-edge #{[0 0] [1 0] [2 0] [3 0] [4 0]})
(def lower-edge #{[0 4] [1 4] [2 4] [3 4] [4 4]})

(def left-grid [1 2])
(def right-grid [3 2])
(def upper-grid [2 1])
(def lower-grid [2 3])

(defn get-tile [grids level x y]
  (get (get grids level default-grid) [x y]))

(defn gen-directions [level x y]
  (map (fn [next] 
         {:level level
          :coords (map + next [x y])}) directions))

(defn gen-edge-directions [edges level]
  (map (fn [next] 
         {:level level
          :coords next}) edges))

(defn all-edge-directions [level x y & grid-poses]
  (concat (gen-directions level x y) (map (fn [next] {:level (dec level) :coords next}) grid-poses)))

(defn bugs-around2 [grids level x y]
  (count
   (filter #(= % \#)
           (map (fn [next] (let [level (:level next)
                                 [x y] (:coords next)]
                             (get-tile grids level x y)))
                (cond
                  (contains? left-edge-mis [x y])
                  (all-edge-directions level x y left-grid)
                  
                  (contains? right-edge-mis [x y])
                  (all-edge-directions level x y right-grid)
                  
                  (contains? upper-edge-mis [x y])
                  (all-edge-directions level x y upper-grid)
                  
                  (contains? lower-edge-mis [x y])
                  (all-edge-directions level x y lower-grid)
                  
                  (= top-left [x y])
                  (all-edge-directions level x y left-grid upper-grid)
                  
                  (= top-right [x y])
                  (all-edge-directions level x y upper-grid right-grid)
                  
                  (= bottom-left [x y])
                  (all-edge-directions level x y left-grid lower-grid)

                  (= bottom-right [x y])
                  (all-edge-directions level x y right-grid lower-grid)

                  (= left-grid [x y])
                  (concat (gen-directions level x y) (gen-edge-directions left-edge (inc level)))
                  
                  (= right-grid [x y]) 
                  (concat (gen-directions level x y) (gen-edge-directions right-edge (inc level)))
                  
                  (= upper-grid [x y]) 
                  (concat (gen-directions level x y) (gen-edge-directions upper-edge (inc level)))
                  
                  (= lower-grid [x y]) 
                  (concat (gen-directions level x y) (gen-edge-directions lower-edge (inc level)))
                  
                  :else 
                  (gen-directions level x y))))))

(defn transform2 [grids level x y]
  (let [curr (get-tile grids level x y)
        num-bugs (bugs-around2 grids level x y)]
    (case curr
      \space \space
      \. (if (or (= num-bugs 1) (= num-bugs 2)) \# \.)
      \# (if (= num-bugs 1) \# \.))))

(def part2-start-grids {0 part2-initial-grid})

(defn gen-levels [grids]
  (let [levels (map #(first %) grids)]
    [(dec (apply min levels))
     (+ (apply max levels) 2)]))

(defn step-one-grid [grids level]
  (reduce (fn [coll next]
            (let [[[x y] _] next
                  next-char (transform2 grids level x y)]
              (assoc coll [x y] next-char))) 
          (get grids level default-grid) 
          (get grids level default-grid)))

(defn step2 [grids]
  (into {} (filter #(not= (second %) default-grid)
          (reduce
           (fn [grids-in-progress level]
             (assoc grids-in-progress level (step-one-grid grids level)))
           grids
           (apply range (gen-levels grids))))))

(defn step2-n [grids n]
  (loop [idx 0
         next-grids grids]
    (if (= idx n) 
      next-grids
      (recur (inc idx) (step2 next-grids)))))

(def final-part2
  (step2-n part2-start-grids 200))

(defn count-bugs-one-grid [grid]
  (count (filter #(= (second %) \#) grid)))

(defn count-bugs-grids [grids]
  (reduce + (map #(count-bugs-one-grid (second %)) grids)))

(println (count-bugs-grids final-part2))