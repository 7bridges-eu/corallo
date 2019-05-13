(ns corallo.operations
  "This namespace includes graph operations such as topological sorting and
  traverse algorithms.")

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
