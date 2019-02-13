(ns clj-graph.operations-test
  (:require [clj-graph.operations :as op]
            [clojure.test :refer :all]))

(def g-test {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                        :b {:value "2" :in #{:a} :out #{:c}}
                        :c {:value "3" :in #{:b} :out #{:d}}
                        :d {:value "4" :in #{:c} :out #{}}}
             :edges {[:a :b] {}
                     [:b :c] {}
                     [:c :d] {}}})

(def g1-test {:vertexes {:a {:value "1" :in #{:d} :out #{:b}}
                         :b {:value "2" :in #{:a} :out #{:c}}
                         :c {:value "3" :in #{:b} :out #{:d}}
                         :d {:value "4" :in #{:c} :out #{:a}}}
              :edges {[:a :b] {}
                      [:b :c] {}
                      [:c :d] {}
                      [:d :a] {}}})

(def g2-test {:vertexes {:a {:value nil, :in #{}, :out #{:b}},
                         :b {:value nil, :in #{:a}, :out #{}},
                         :c {:value nil, :in #{}, :out #{:d}},
                         :d {:value nil, :in #{:c}, :out #{}}}
              :edges {[:a :b] {}
                      [:c :d] {}}})

(deftest find-next-vertex-test
  (testing "Testing find-next-vertex function"
    (let [f #'clj-graph.operations/find-next-vertex]
      (is (= (f (:vertexes g-test) #{:a}) :d)))))

(deftest acyclic-graph?-test
  (testing "Testing acyclic-graph? predicate"
    (is (true? (op/acyclic-graph? g-test)))
    (is (false? (op/acyclic-graph? g1-test)))))

(deftest topo-sort-test
  (testing "Testing topo-sort function"
    (is (= (op/topo-sort g-test) [:a :b :c :d]))
    (is (nil? (op/topo-sort g1-test)))))

(deftest complete-graph?-test
  (testing "Testing complete-graph? predicate"
    (is (true? (op/complete-graph? g-test)))
    (is (false? (op/complete-graph? g2-test)))))
