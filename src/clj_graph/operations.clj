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

(defn traverse
  "Traverse the graph `g` starting from vertex `k`.
  If a circular dependency is found, a map with error data is returned."
  [g k]
  (try
    (circular-dependency? g)
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
    (catch Exception e (ex-data e))))

(defn topo-sort
  "Topologically sort the graph `g`.
  If a circular dependency is found, a map with error data is returned."
  [g]
  (try
    (circular-dependency? g)
    (catch Exception e (ex-data e))))
