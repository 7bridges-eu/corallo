(ns clj-graph.pprint)

(defn- pprint-nodes
  "Return a string representation of the nodes in the graph `g`."
  [g]
  (str (count (keys g))
       " nodes:\n"
       (apply str (->> (keys g) (interpose "\n")))))

(defn- pprint-edges
  "Return a string representation of the edges in the graph `g`."
  [g]
  (let [es (->> (reduce-kv
                 (fn [acc k v]
                   (assoc acc k (reduce
                                 (fn [a el]
                                   (conj a (str k " ➙ " el)))
                                 []
                                 (:nodes v))))
                 {}
                 g)
                vals
                (remove empty?)
                flatten)]
    (apply str (count es) " edges:\n" (->> es (interpose "\n")))))

(defn pprint-graph
  "Return a string representation of the graph `g`."
  [g]
  (str (pprint-nodes g) "\n" (pprint-edges g)))
