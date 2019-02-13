(ns clj-graph.graph
  (:require [clojure.set :as s]))

(defn add-vertex
  "Add a vertex `k` with value `v`. Do not add it if already present."
  [g k v]
  (if (get g k)
    g
    (assoc-in g [:vertexes k] {:value v :in #{} :out #{}})))

(defn add-edge-in
  [g k1 k2]
  (update-in g [:vertexes k1 :in] conj k2))

(defn add-edge-out
  [g k1 k2]
  (update-in g [:vertexes k1 :out] conj k2))

(defn add-edge
  "Add an edge in from the vertex `to` to the vertex `from` in the graph `g`.
  Add an edge out from the vertex `from` to the vertex `to` in the graph `g`.
  Add edge `properties` or empty map if `properties` is not passed."
  ([g from to]
   (add-edge g from to {}))
  ([g from to properties]
   (-> g
       (add-edge-in to from)
       (add-edge-out from to)
       (assoc-in [:edges [from to]] properties))))

(defn remove-edge-in
  [g k1 k2]
  (let [k1-ins (get-in g [:vertexes k1 :in])]
    (->> (s/difference k1-ins #{k2})
         (assoc-in g [:vertexes k1 :in]))))

(defn remove-edge-out
  [g k1 k2]
  (let [k1-ins (get-in g [:vertexes k1 :out])]
    (->> (s/difference k1-ins #{k2})
         (assoc-in g [:vertexes k1 :out]))))

(defn remove-edge
  "Remove edge (in and out) from `k1` to `k2`."
  [g k1 k2]
  (-> g
      (remove-edge-in k2 k1)
      (remove-edge-out k1 k2)
      (update :edges dissoc [k1 k2])))

(defn in-edges
  [g k]
  (get-in g [:vertexes k :in]))

(defn out-edges
  [g k]
  (get-in g [:vertexes k :out]))

(defn neighbors
  "Get all the nodes with an edge to `k` from the graph `g`."
  [g k]
  (s/union (in-edges g k) (out-edges g k)))

(defn adjacent?
  "Check there is an edge between `k1` and `k2`."
  [g k1 k2]
  (true? (some #(= % k2) (neighbors g k1))))

(defn- remove-from-ins
  [g ins k]
  (reduce (fn [acc el]
            (remove-edge acc el k))
          g
          ins))

(defn- remove-from-outs
  [g outs k]
  (reduce (fn [acc el]
            (remove-edge acc k el))
          g
          outs))

(defn- remove-edges
  "Remove all the edges containing `k` from the graph `g`."
  [g k]
  (reduce-kv (fn [m k' v]
               (if (some #{k} k')
                 m
                 (assoc-in m [:edges k'] v)))
             g
             (:edges g)))

(defn remove-vertex
  "Remove vertex `k` from graph `g`."
  [g k]
  (let [k-ins (get-in g [:vertexes k :in])
        k-outs (get-in g [:vertexes k :out])]
    (-> g
        (remove-from-ins k-ins k)
        (remove-from-outs k-outs k)
        (remove-edges k)
        (update :vertexes dissoc k))))

(defn get-vertex-value
  "Get the value of the vertex `k` in the graph `g`."
  [g k]
  (get-in g [:vertexes k :value] nil))

(defn set-vertex-value
  "Set the value of the vertex `k` in the graph `g`."
  [g k v]
  (assoc-in g [:vertexes k :value] v))

(defn get-edge-properties
  "Get the properties of the edge between `k1` and `k2` in the graph `g`."
  [g k1 k2]
  (get-in g [:edges [k1 k2]] nil))

(defn set-edge-properties
  "Update the properties of the edge between `k1` and `k2` in the graph `g`."
  [g k1 k2 properties]
  (assoc-in g [:edges [k1 k2]] properties))
