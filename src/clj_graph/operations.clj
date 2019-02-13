(ns clj-graph.operations
  (:require [clj-graph.graph :as graph]))

(defn- find-unused-vertexes
  "Find elements in `vertexes` which are not in `used-vertexes`."
  [vertexes used-vertexes]
  (reduce (fn [acc el]
            (let [k' (first (keys el))]
              (if (contains? used-vertexes k')
                acc
                (conj acc k'))))
          []
          vertexes))

(defn- find-next-vertex
  "Check if a vertex in `g` is already present in `used-vertexes`.
  If present return nil, otherwise returned the vertex key."
  [g used-vertexes]
  (some (fn [[k v]]
          (let [unused-vertexes (find-unused-vertexes (:out v) used-vertexes)]
            (when (empty? unused-vertexes)
              k)))
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

(defn complete-graph?
  "Determine if graph `g` is a complete graph.
  See: https://en.wikipedia.org/wiki/Complete_graph"
  [g]
  (let [g' (topo-sort g)
        ins (mapcat keys (mapcat :in (vals g)))]
    (reduce (fn [_ el]
              (if (some #{el} ins)
                true
                (reduced false)))
            (butlast g'))))
