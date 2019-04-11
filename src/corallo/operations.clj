(ns corallo.operations
  "This namespace includes graph operations such as topological sorting,
  traverse algorithms, and rendering."
  (:require [corallo.graph :as graph]
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

(defn render-graph-stream
  "Output the render image of the graph `g` to a byte array."
  [g]
  (let [nodes (keys (:vertexes g))
        raw-edges (keys (:edges g))
        edges (mapv (fn [[a b]] [(name a) (name b)]) raw-edges)]
    (with-open [out (java.io.ByteArrayOutputStream.)]
      (-> (t/graph->dot nodes edges {:directed? true
                                     :node->id #(name %)})
          (t/dot->image "png")
          (io/copy out))
      (.toByteArray out))))

(defn render-graph-file
  "Output the render image of the graph `g` to `output-path`.
  Since `render-graph-file` generates a PNG file, `output-path` must point at a
  PNG file, otherwise `render-graph-file` does not create an image file."
  [g output-path]
  (when (re-find #"(?i).png" output-path)
    (let [nodes (keys (:vertexes g))
          raw-edges (keys (:edges g))
          edges (mapv (fn [[a b]] [(name a) (name b)]) raw-edges)]
      (-> (t/graph->dot nodes edges {:directed? true
                                     :node->id #(name %)})
          (t/dot->image "png")
          (io/copy (io/file output-path))))))
