(ns clj-graph.pprint-test
  (:require [clj-graph.pprint :as pp]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :in #{} :out #{:b}}
             :b {:value "2" :in #{:a} :out #{:c}}
             :c {:value "3" :in #{:b} :out #{:d}}
             :d {:value "4" :in #{:c} :out #{}}})

(deftest pprint-nodes-test
  (testing "Testing pprint-nodes function"
    (let [f #'clj-graph.pprint/pprint-nodes]
      (is (= (f g-test) "4 nodes:\n:a\n:b\n:c\n:d")))))

(deftest pprint-edges-test
  (testing "Testing pprint-edges function"
    (let [f #'clj-graph.pprint/pprint-edges]
      (is (= (f g-test) "3 edges in:\n:b ➙ :a\n:c ➙ :b\n:d ➙ :c3 edges out:\n:a ➙ :b\n:b ➙ :c\n:c ➙ :d")))))

(deftest pprint-graph
  (testing "Testing ppring-graph function"
    (is (= (pp/pprint-graph g-test)
           "4 nodes:\n:a\n:b\n:c\n:d\n3 edges in:\n:b ➙ :a\n:c ➙ :b\n:d ➙ :c3 edges out:\n:a ➙ :b\n:b ➙ :c\n:c ➙ :d"))))
