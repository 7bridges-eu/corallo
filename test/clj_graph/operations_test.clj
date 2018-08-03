(ns clj-graph.operations-test
  (:require [clj-graph.operations :as op]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :nodes #{}}
             :b {:value "2" :nodes #{:a}}
             :c {:value "3" :nodes #{:b}}
             :d {:value "4" :nodes #{:c :b}}})

(def g1-test {:a {:value "1" :nodes #{}}
              :b {:value "2" :nodes #{:a}}
              :c {:value "3" :nodes #{:b :d}}
              :d {:value "4" :nodes #{:c :b}}})

(deftest find-next-vertex-test
  (testing "Testing find-next-vertex function"
    (let [f #'clj-graph.operations/find-next-vertex]
      (is (= (f g-test #{:a}) :a)))))

(deftest circular-dependency?-test
  (testing "Testing circular-dependency? function"
    (is (= (op/circular-dependency? g-test) [:a :b :c :d]))
    (is (thrown? Exception (op/circular-dependency? g1-test)))))

(deftest traverse-test
  (testing "Testing traverse function"
    (is (= (op/traverse g-test :d) [:d :c :b :a :b :a]))))

(deftest topo-sort-test
  (testing "Testing topo-sort function"
    (is (= (op/topo-sort g-test) [:a :b :c :d]))
    (is (= (op/topo-sort g1-test)
           {:message "Circular dependency",
            :causes
            {:c {:value "3", :nodes #{:b :d}},
             :d {:value "4", :nodes #{:c :b}}}}))))
