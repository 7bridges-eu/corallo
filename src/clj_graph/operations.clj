(ns clj-graph.operations
  (:require [clj-graph.graph :as graph]
            [clojure.java.io :as io]
            [tangle.core :as t]))

(defn- find-next-vertex
  "Check if a vertex in `g` is already present in `used-vertexes`.
  If present return nil, otherwise returned the vertex key."
  [g used-vertexes]
  (some (fn [[k v]]
          (let [unused-vertexes (remove used-vertexes (:out v))]
            (when (empty? unused-vertexes)
              k)))
        g))

(defn topo-sort
  "Sort `g` topologically.
  If a circular dependency is found, return nil."
  [g]
  (loop [g (:vertexes g)
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
        ins (mapcat :in (vals (:vertexes g)))]
    (reduce (fn [_ el]
              (if (some #{el} ins)
                true
                (reduced false)))
            (butlast g'))))

(defn render-graph
  "Output the render image of the graph `g`."
  [g output-path]
  (let [nodes (keys (:vertexes g))
        edges (keys (:edges g))]
    (-> (t/graph->dot nodes edges {:directed? true})
        (t/dot->image "png")
        (io/copy (io/file output-path)))))
