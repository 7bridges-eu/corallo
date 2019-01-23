(ns clj-graph.operations-test
  (:require [clj-graph.operations :as op]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :in #{} :out #{:b}}
             :b {:value "2" :in #{:a} :out #{:c}}
             :c {:value "3" :in #{:b} :out #{:d}}
             :d {:value "4" :in #{:c} :out #{}}})

(def g1-test {:a {:value "1" :in #{:d} :out #{:b}}
              :b {:value "2" :in #{:a} :out #{:c}}
              :c {:value "3" :in #{:b} :out #{:d}}
              :d {:value "4" :in #{:c} :out #{:a}}})

(deftest find-next-vertex-test
  (testing "Testing find-next-vertex function"
    (let [f #'clj-graph.operations/find-next-vertex]
      (is (= (f g-test #{:a}) :d)))))

(deftest circular-dependency?-test
  (testing "Testing circular-dependency? function"
    (is (= (op/circular-dependency? g-test) [:d :c :b :a]))
    (is (thrown? Exception (op/circular-dependency? g1-test)))))

(deftest traverse-test
  (testing "Testing traverse function"
    (is (= (op/traverse g-test :a) [:a :b :c :d]))))

(deftest topo-sort-test
  (testing "Testing topo-sort function"
    (is (= (op/topo-sort g-test) [:d :c :b :a]))
    (is (= (op/topo-sort g1-test)
           {:message "Circular dependency",
            :causes
            {:a {:value "1", :in #{:d}, :out #{:b}},
             :b {:value "2", :in #{:a}, :out #{:c}},
             :c {:value "3", :in #{:b}, :out #{:d}},
             :d {:value "4", :in #{:c}, :out #{:a}}}}))))
