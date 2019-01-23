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
  (let [es-in (->> (reduce-kv
                    (fn [acc k v]
                      (assoc acc k (reduce
                                    (fn [a el]
                                      (conj a (str k " ➙ " el)))
                                    []
                                    (:in v))))
                    {}
                    g)
                   vals
                   (remove empty?)
                   flatten)
        es-out (->> (reduce-kv
                     (fn [acc k v]
                       (assoc acc k (reduce
                                     (fn [a el]
                                       (conj a (str k " ➙ " el)))
                                     []
                                     (:out v))))
                     {}
                     g)
                    vals
                    (remove empty?)
                    flatten)]
    (str
     (apply str (count es-in) " edges in:\n" (->> es-in (interpose "\n")))
     (apply str (count es-out) " edges out:\n" (->> es-out (interpose "\n"))))))

(defn pprint-graph
  "Return a string representation of the graph `g`."
  [g]
  (str (pprint-nodes g) "\n" (pprint-edges g)))
