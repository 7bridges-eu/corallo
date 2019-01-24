(ns clj-graph.operations
  (:require [clj-graph.graph :as graph]))

(defn- find-next-vertex
  "Check if a vertex in `g` is already present in `used-vertexes`.
  If present return nil, otherwise returned the vertex key."
  [g used-vertexes]
  (some (fn [[k v]]
          (when (empty? (remove used-vertexes (:in v)))
            k))
        g))

(defn circular-dependency?
  "Determine if there is a circular dependency in the graph `g`."
  [g]
  (loop [g g
         acc []]
    (if (empty? g)
      acc
      (if-let [n (find-next-vertex g (set acc))]
        (recur (dissoc g n) (conj acc n))
        (throw
         (ex-info "Circular dependency"
                  {:message "Circular dependency"
                   :causes g}))))))

(defn topo-sort
  "Topologically sort the graph `g`.
  If a circular dependency is found, a map with error data is returned."
  [g]
  (try
    (circular-dependency? g)
    (catch Exception e (ex-data e))))

(defn traverse
  "Traverse the graph `g` starting from vertex `k`.
  If a circular dependency is found, a map with error data is returned."
  [g k]
  (let [g (topo-sort g)
        ns (graph/out-edges g k)]
    (if (empty? ns)
      [k]
      (reduce
       (fn [acc el]
         (if (empty? (graph/out-edges g el))
           (conj acc el)
           (into acc (traverse g el))))
       [k]
       ns))))

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
