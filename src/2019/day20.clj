(ns advent2019.day20
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day20"
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

(def initial-maze
  (reduce (fn [coll pair]
            (let [[y line] pair]
              (apply conj coll (convert-line line y)))) {} (pair-with-index data)))

(def not-nil? (complement nil?))

(def directions
  [[0 1]
   [0 -1]
   [-1 0]
   [1 0]])

(defn portal-coords [maze letter-coords]
  (first (filter not-nil? (for [dir directions]
                   (let [new-coords (map + letter-coords dir)]
                     (if (= (get maze new-coords) \.) 
                       (into (vector) new-coords) 
                       nil))))))

(defn is-letter? [ch]
  (let [c (int ch)]
    (and (>= c 65) (<= c 90))))

(defn other-letter-coords [maze letter-coords]
  (first (filter not-nil? (for [dir directions]
                   (let [new-coords (map + letter-coords dir)
                         c (int (get maze new-coords -1))]
                     (if (is-letter? c)
                       (into (vector) new-coords)
                       nil))))))


(defn portal [maze letter-coords]
  (let [letter-coords2 (other-letter-coords maze letter-coords)
        portal-cs (or (portal-coords maze letter-coords) (portal-coords maze letter-coords2))
        [l1 l2] (sort (vector letter-coords letter-coords2))
        letter-str (str (get maze l1) (get maze l2))]
    [letter-str portal-cs]
    ))

(defn portals-set [maze]
  (into #{} (map #(portal maze (first %))
       (filter (fn [pair] (let [[_ c] pair]
                            (is-letter? c))) maze))))

(def all-portals-set
  (portals-set initial-maze))

(def all-portals-map
  (into {} all-portals-set))

(defn portal-to-portals [portals-set]
  (reduce 
   (fn [coll next] (let [[_ [a b]] next]
                     (assoc (assoc coll a b) b a)))
   {}
   (filter #(= (count (last %)) 2)
           (reduce (fn [coll next]
                     (let [[name coords] next
                           curr (get coll name [])]
                       (assoc coll name (conj curr coords)))) {} portals-set))))

(def p2p-set 
  (portal-to-portals all-portals-set))

(def start-coords
  (get all-portals-map "AA"))

(def end-coords
  (get all-portals-map "ZZ"))

(defn generate-nodes [maze portals curr seen]
  (let [coords (:coords curr)
        dist (:dist curr)]
    (filter (fn [node] (let [cs (:coords node)
                             c (int (get maze cs))]
                         (and
                          (not (contains? seen cs))
                          (= c (int \.)))))
            (let [new-nodes (map (fn [dir]
                                   (let [new-coords (map + coords dir)]
                                     {:coords (into (vector) new-coords)
                                      :dist (inc dist)})) directions)
                  portal-new-coords (get portals coords)]
              (if (nil? portal-new-coords) new-nodes (conj new-nodes
                                                           {:coords (into (vector) portal-new-coords)
                                                            :dist (inc dist)}))))))

(defn bfs [maze
           portals
           curr-coords
           end-coords
           curr-dist]
  (loop [queue [{:coords curr-coords
                 :dist curr-dist}]
         seen #{curr-coords}]
    (when (seq queue)
      (let [[node & other-nodes-temp] queue
            other-nodes (into (vector) other-nodes-temp)
            new-nodes (generate-nodes maze portals node seen)
            new-queue (reduce conj other-nodes new-nodes)
            new-seen (reduce conj seen (map #(into (vector) (:coords %)) new-nodes))]
        (if (= (:coords node) end-coords)
          (:dist node)
          (recur new-queue new-seen))))))

(println (bfs initial-maze p2p-set start-coords end-coords 0))

(def x-range
  (let [xs (map #(first (second %)) all-portals-set)]
    [(apply min xs) (apply max xs)]))

(def y-range
  (let [ys (map #(second (second %)) all-portals-set)]
    [(apply min ys) (apply max ys)]))

(defn outer-portal? [coords]
  (let [[x y] coords
        [x-min x-max] x-range
        [y-min y-max] y-range]
    (or (= x x-min) (= x x-max)
        (= y y-min) (= y y-max))))

(defn generate-nodes2 [maze portals curr seen]
  (let [coords (:coords curr)
        dist (:dist curr)
        level (:level curr)]
    (filter (fn [node] (let [cs (:coords node)
                             l (:level node)
                             c (int (get maze cs))]
                         (and
                          (not (contains? seen [cs l]))
                          (>= l 0)
                          (= c (int \.)))))
            (let [new-nodes (map (fn [dir]
                                   (let [new-coords (map + coords dir)]
                                     {:coords (into (vector) new-coords)
                                      :dist (inc dist)
                                      :level level})) directions)
                  portal-new-coords (get portals coords)]
              (if (nil? portal-new-coords) new-nodes (conj new-nodes
                                                           {:coords (into (vector) portal-new-coords)
                                                            :dist (inc dist)
                                                            :level (if (outer-portal? coords) 
                                                                     (dec level)
                                                                     (inc level))}))))))

(defn bfs2 [maze
           portals
           curr-coords
           end-coords
           curr-dist]
  (loop [queue [{:coords curr-coords
                 :dist curr-dist
                 :level 0}]
         seen #{[curr-coords 0]}]
    (when (seq queue)
      (let [[node & other-nodes-temp] queue
            other-nodes (into (vector) other-nodes-temp)
            new-nodes (generate-nodes2 maze portals node seen)
            new-queue (reduce conj other-nodes new-nodes)
            new-seen (reduce conj seen (map #(vector (:coords %) (:level %)) new-nodes))]
        (if (and (= (:coords node) end-coords) (= (:level node) 0))
          (:dist node)
          (recur new-queue new-seen))))))

(println (bfs2 initial-maze p2p-set start-coords end-coords 0))