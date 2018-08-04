(ns clj-graph.graph)

(defn adjacent?
  "Check there is an edge between `k1` and `k2`."
  [g k1 k2]
  (true? (some #(= % k2) (get-in g [k1 :nodes]))))

(defn neighbors
  "Get all the nodes with an edge to `k` from the graph `g`."
  [g k]
  (get-in g [k :nodes]))

(defn add-vertex
  "Add a vertex `k` with value `v` and linked nodes `ns` to the graph `g`."
  [g k v ns]
  (assoc g k {:value v :nodes (set ns)}))

(defn remove-vertex
  "Remove vertex `k` from the graph `g`.
  Remove `k` from :nodes of linked vertexes as well."
  [g k]
  (->> (dissoc g k)
       (reduce-kv
        (fn [m k1 v]
          (if (some #(= % k) (:nodes v))
            (->> (remove #{k} (:nodes v))
                 (assoc-in m [k1 :nodes]))
            (assoc m k1 v)))
        {})))

(defn add-edge
  "Add an edge from the vertex `k1` to the vertex `k2` in the graph `g`."
  [g k1 k2]
  (update-in g [k1 :nodes] conj k2))

(defn remove-edge
  "Remove the edge from vertex `k1` to the vertex `k2` in the graph `g`."
  [g k1 k2]
  (let [k1-nodes (get-in g [k1 :nodes])]
    (->> k1-nodes
         (remove #(= % k2))
         set
         (assoc-in g [k1 :nodes]))))

(defn get-vertex-value
  "Get the value of the vertex `k` in the graph `g`."
  [g k]
  (get-in g [k :value] nil))

(defn set-vertex-value
  "Set the value of the vertex `k` in the graph `g`."
  [g k v]
  (assoc-in g [k :value] v))
