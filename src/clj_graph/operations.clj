(ns clj-graph.operations
  (:require [clj-graph.graph :as graph]))

(defn- find-next-vertex
  "Check if a vertex in `g` is already present in `used-vertexes`.
  If present return nil, otherwise returned the vertex key."
  [g used-vertexes]
  (some (fn [[k v]]
          (when (empty? (remove used-vertexes (:out v)))
            k))
        g))

(defn topo-sort
  "Sort `g` topologically.
  If a circular dependency is found, return nil."
  [g]
  (loop [g g
         acc []]
    (if (empty? g)
      (reverse acc)
      (if-let [n (find-next-vertex g (set acc))]
        (recur (dissoc g n) (conj acc n))
        nil))))

(defn acyclic-graph?
  "Check if there is not a circular dependency in `g`."
  [g]
  (not (nil? (topo-sort g))))

(defn traverse
  "Traverse the graph `g` starting from vertex `k` if topo-sorted.
  Otherwise, return `g`."
  [g k]
  (if (acyclic-graph? g)
    (let [ns (graph/out-edges g k)]
      (if (empty? ns)
        [k]
        (reduce
         (fn [acc el]
           (if (empty? (graph/out-edges g el))
             (conj acc el)
             (into acc (traverse g el))))
         [k]
         ns)))
    g))

(defn complete-graph?
  "Determine if graph `g` is a complete graph.
  See: https://en.wikipedia.org/wiki/Complete_graph"
  [g]
  (let [g' (topo-sort g)
        ins (mapcat :in (vals g))]
    (reduce (fn [_ el]
              (if (some #{el} ins)
                true
                (reduced false)))
            (butlast g'))))
