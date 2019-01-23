(ns clj-graph.graph
  (:require [clojure.set :as s]))

(defn add-vertex
  "Add a vertex `k` with value `v` and linked nodes `ns` to the graph `g`."
  [g k v]
  (if (get g k)
    g
    (assoc g k {:value v :in #{} :out #{}})))

(defn add-edge-in
  [g k1 k2]
  (update-in g [k1 :in] conj k2))

(defn add-edge-out
  [g k1 k2]
  (update-in g [k1 :out] conj k2))

(defn add-edge
  "Add an edge in from the vertex `to` to the vertex `from` in the graph `g`.
  Add an edge out from the vertex `from` to the vertex `to` in the graph `g`."
  [g from to]
  (-> g
      (add-edge-in to from)
      (add-edge-out from to)))

(defn remove-edge-in
  [g k1 k2]
  (let [k1-ins (get-in g [k1 :in])]
    (->> (s/difference k1-ins #{k2})
         (assoc-in g [k1 :in]))))

(defn remove-edge-out
  [g k1 k2]
  (let [k1-ins (get-in g [k1 :out])]
    (->> (s/difference k1-ins #{k2})
         (assoc-in g [k1 :out]))))

(defn remove-edge
  [g k1 k2]
  (-> g
      (remove-edge-in k2 k1)
      (remove-edge-out k1 k2)))

(defn in-edges
  [g k]
  (get-in g [k :in]))

(defn out-edges
  [g k]
  (get-in g [k :out]))

(defn neighbors
  "Get all the nodes with an edge to `k` from the graph `g`."
  [g k]
  (s/union (in-edges g k)
           (out-edges g k)))

(defn adjacent?
  "Check there is an edge between `k1` and `k2`."
  [g k1 k2]
  (true? (some #(= % k2) (neighbors g k1))))

(defn remove-from-ins
  [g ins k]
  (reduce (fn [acc el]
            (remove-edge acc el k))
          g
          ins))

(defn remove-from-outs
  [g outs k]
  (reduce (fn [acc el]
            (remove-edge acc k el))
          g
          outs))

(defn remove-vertex
  [g k]
  (let [k-ins (get-in g [k :in])
        k-outs (get-in g [k :out])]
    (-> g
        (remove-from-ins k-ins k)
        (remove-from-outs k-outs k)
        (dissoc k))))

(defn get-vertex-value
  "Get the value of the vertex `k` in the graph `g`."
  [g k]
  (get-in g [k :value] nil))

(defn set-vertex-value
  "Set the value of the vertex `k` in the graph `g`."
  [g k v]
  (assoc-in g [k :value] v))
