(ns clj-graph.pprint-test
  (:require [clj-graph.pprint :as pp]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :nodes #{}}
             :b {:value "2" :nodes #{:a}}
             :c {:value "3" :nodes #{:b}}
             :d {:value "4" :nodes #{:c :b}}})

(deftest pprint-nodes-test
  (testing "Testing pprint-nodes function"
    (let [f #'clj-graph.pprint/pprint-nodes]
      (is (= (f g-test) "4 nodes:\n:a\n:b\n:c\n:d")))))

(deftest pprint-edges-test
  (testing "Testing pprint-edges function"
    (let [f #'clj-graph.pprint/pprint-edges]
      (is (= (f g-test) "4 edges:\n:b ➙ :a\n:c ➙ :b\n:d ➙ :c\n:d ➙ :b")))))

(deftest pprint-graph
  (testing "Testing ppring-graph function"
    (is (= (pp/pprint-graph g-test)
           (str "4 nodes:\n:a\n:b\n:c\n:d"
                "\n"
                "4 edges:\n:b ➙ :a\n:c ➙ :b\n:d ➙ :c\n:d ➙ :b")))))
