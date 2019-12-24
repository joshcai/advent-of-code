(ns advent2019.day18
  (:require
   [clojure.java.io :as io]))

(def data
  (->> "2019/day18"
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

(def initial-map
  (reduce (fn [coll pair] 
            (let [[y line] pair]
              (apply conj coll (convert-line line y))
              )) {} (pair-with-index data)))

(def key-map 
  (reduce (fn [coll next] (apply assoc coll (reverse next))) {} initial-map))

(def start-coords
  (get key-map \@))

(def directions
  [[0 1]
   [0 -1]
   [-1 0]
   [1 0]])

(defn valid-maze [maze]
  (zero? (count (filter (fn [pair] (let [c (int (second pair))] 
                       (and (>= c 97) (<= c 122)))) maze))))

(defn update-maze [maze keys]
  (reduce (fn [coll key] (assoc coll (get key-map (char key)) \.)) maze keys))

(defn generate-nodes [maze-start keys curr seen]
  (let [maze (update-maze maze-start keys)
        coords (:coords curr)
        dist (:dist curr)]
    (filter (fn [node] (let [cs (:coords node)
                             c (int (get maze cs))]
                         (and
                          (not (contains? seen [cs keys]))
                          (contains? maze cs)
                          (not= c (int \#))
                          (not (and (>= c 65) (<= c 90) (not (contains? keys (char (+ c 32))))))
                          )))
            (map (fn [dir]
                   (let [new-coords (map + coords dir)
                         intc (int (get maze new-coords))]
                     {:coords new-coords
                      :dist (inc dist)
                      :keys (if (and (>= intc 97) (<= intc 122))
                              (conj keys (get maze new-coords))
                              keys)})) directions))))

(defn bfs [maze
           keys
           curr-coords
           curr-dist]
  (loop [queue [{:coords curr-coords
                 :dist curr-dist
                 :keys keys}]
         seen #{[curr-coords keys]}
         candidates #{}]
    (when (seq queue)
      (let [[node & other-nodes-temp] queue
            other-nodes (into (vector) other-nodes-temp)
            new-queue (reduce conj other-nodes (generate-nodes maze (:keys node) node seen))
            new-seen (reduce conj seen (map #(vector (map + (:coords node) %) (:keys node)) directions))]
        (if (= (count (:keys node)) 26)
          (:dist node)
          (recur new-queue new-seen candidates))))))

(println (bfs initial-map #{} start-coords 0))

(defn generate-nodes2 [maze-start keys curr seen]
  (let [maze (update-maze maze-start keys)
        coords (:coords curr)
        dist (:dist curr)]
    (apply concat
           (for [i (range (count coords))]
             (let [coords-curr (nth coords i)]
               (map #(assoc % :coords (assoc coords i (:coords %)))
                    (filter (fn [node] (let [cs (:coords node)
                                             c (int (get maze cs))]
                                         (and
                                          (not (contains? seen [cs keys]))
                                          (contains? maze cs)
                                          (not= c (int \#))
                                          (not (and (>= c 65) (<= c 90) (not (contains? keys (char (+ c 32)))))))))
                            (map (fn [dir]
                                   (let [new-coords (map + coords-curr dir)
                                         intc (int (get maze new-coords))]
                                     {:coords new-coords
                                      :dist (inc dist)
                                      :keys (if (and (>= intc 97) (<= intc 122))
                                              (conj keys (get maze new-coords))
                                              keys)})) directions))))))))


(defn gen-seen [seen node]
  (reduce conj seen 
          (map #(vector % (:keys node))
          (for [coords-pair (:coords node)
                dir directions] (map + coords-pair dir)))))

(defn bfs2 [maze
           keys
           curr-coords  ; array of four coord pairs now
           curr-dist]
  (loop [queue [{:coords curr-coords
                 :dist curr-dist
                 :keys keys}]
         seen (into #{} (map #(vector % keys) curr-coords))
         candidates #{}]
    (when (seq queue)
      (let [[node & other-nodes-temp] queue
            other-nodes (into (vector) other-nodes-temp)
            new-queue (reduce conj other-nodes (generate-nodes2 maze (:keys node) node seen))
            new-seen (gen-seen seen node)]
        (if (= (count (:keys node)) 26)
          (:dist node)
          (recur new-queue new-seen candidates))))))

(def part2-map 
  (reduce (fn [coll next] (assoc coll (first next) (second next)))
          initial-map
          [
           [[40 40] \#]
           [[40 39] \#]
           [[40 41] \#]
           [[39 40] \#]
           [[41 40] \#]
           ]))

(def start1 [39 39])
(def start2 [39 41])
(def start3 [41 39])
(def start4 [41 41])

(println (bfs2 part2-map #{} [start1 start2 start3 start4] 0))

;; dfs approach was too slow even after some hacky optimizations :(
(def infinity 1000000000)

(defn generate-nodes3 [maze keys curr seen]
  (let [coords (:coords curr)
        dist (:dist curr)]
    (filter (fn [node] (let [cs (:coords node)
                             c (int (get maze cs))]
                         (and
                          (not (contains? seen cs))
                          (contains? maze cs)
                          (not= c (int \#))
                          (not (and (>= c 65) (<= c 90) (not (contains? keys (+ c 32)))))
                          )))
            (map (fn [dir]
                   {:coords (map + coords dir)
                    :dist (inc dist)}) directions))))

(defn valid-candidate [maze keys node]
  (let [c (int (get maze (:coords node)))]
    (if (and (>= c 97) (<= c 122)) c
        (if (and (>= c 65) (<= c 90) (contains? keys (+ c 32))) c
            nil))))

(defn generate-candidates [maze
                           keys
                           curr-coords
                           curr-dist]
  (loop [queue [{:coords curr-coords
                 :dist curr-dist}]
         seen #{curr-coords}
         candidates #{}]
    (if (seq queue)
      (let [[node & other-nodes-temp] queue
            other-nodes (into (vector) other-nodes-temp)
            new-queue (reduce conj other-nodes (generate-nodes3 maze keys node seen))
            new-seen (reduce conj seen (map #(map + (:coords node) %) directions))
            valid (valid-candidate maze keys node)]
          (if (nil? valid)
            (recur new-queue new-seen candidates)
            (recur other-nodes seen (conj candidates node))))
      candidates)))


(defn dfs [maze
           start-c
           keys
           curr-coords
           curr-dist
           curr-dist-total
           min-dist-start
           cache]  ; cache is a map with key [<keys> <key to get>]
  (let [candidates 
        (sort-by :dist 
                 (filter #(<= (+ curr-dist-total (:dist %)) min-dist-start) 
                         (generate-candidates maze keys curr-coords 0)))]
    (if (empty? candidates)
      (if (valid-maze maze) 
        [curr-dist (+ (min min-dist-start curr-dist) curr-dist-total) cache] 
        [infinity infinity cache])
      (let [[new-min new-min-total new-cache]
            (reduce (fn [coll node]
                      (let [[min-dist min-dist-total coll-cache] coll
                            coords (:coords node)
                            dist (:dist node)
                            c (int (get maze coords))
                            cached-dfs (get coll-cache [keys start-c c] nil)]
                        (if (number? cached-dfs)
                          [(min min-dist cached-dfs) (min (+ (min min-dist cached-dfs) curr-dist-total) min-dist-total) coll-cache]
                          (if (< min-dist dist)
                            coll
                            (let [[next-dist next-min-total next-cache]
                                  (dfs (assoc maze (:coords node) \.) c (conj keys c) coords dist (+ curr-dist-total dist) min-dist-total coll-cache)]
                              [(min min-dist next-dist) (min (+ (min min-dist next-dist) curr-dist-total) next-min-total) (assoc next-cache [keys start-c c] next-dist)])))))
                    [infinity infinity cache]
                    candidates)]
        [(+ new-min curr-dist) new-min-total new-cache]))))

; (println (first (dfs initial-map (int \@) #{} start-coords 0 0 infinity {})))
