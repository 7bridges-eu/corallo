(ns corallo.render
  "This namespace includes operations for graph rendering."
  (:require [clojure.java.io :as io]
            [tangle.core :as t]))

(defn- render-graph
  "Render the graph `g` to a PNG file."
  [g]
  (let [nodes (keys (:vertexes g))
        raw-edges (keys (:edges g))
        edges (mapv (fn [[a b]] [(name a) (name b)]) raw-edges)]
    (-> (t/graph->dot nodes edges {:directed? true
                                   :node->id #(name %)})
        (t/dot->image "png"))))

(defn graph->byte-array
  "Output the render image of the graph `g` to a byte array."
  [g]
  (with-open [out (java.io.ByteArrayOutputStream.)]
    (-> g
        render-graph
        (io/copy out))
    (.toByteArray out)))

(defn graph->png
  "Output the render image of the graph `g` to `output-path`.
  Since `graph->png` generates a PNG file, `output-path` must point at a PNG
  file, otherwise `graph->png` does not create an image file."
  [g output-path]
  (when (re-find #"(?i).png" output-path)
    (-> g
        render-graph
        (io/copy (io/file output-path)))))
