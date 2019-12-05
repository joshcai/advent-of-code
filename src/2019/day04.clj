(ns advent2019.day03)

(defn digits [n]
  (if (pos? n)
    (conj (digits (quot n 10)) (mod n 10))
    []))

(defn never-decrease? [digits] 
  (apply = true (map #(<= (first %) (last %)) (partition 2 1 digits))))

(defn consecutive? [digits] 
  (some true? (map #(= (first %) (last %)) (partition 2 1 digits))))

(defn valid? [digits]
  (and (never-decrease? digits) (consecutive? digits)))

(def part1 (count (filter valid? (map digits (range 134792 675810)))))
(println part1)
 
(defn pack-digits [digits] 
  (partition-by identity digits))

(defn only-two-consecutive? [digits]
  (some #(= (count %) 2) (pack-digits digits)))

(defn valid-part2? [digits]
  (and (never-decrease? digits) (only-two-consecutive? digits)))

(def part2 (count (filter valid-part2? (map digits (range 134792 675810)))))
(println part2)