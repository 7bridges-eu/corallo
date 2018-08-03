(ns clj-graph.graph-test
  (:require [clj-graph.graph :as cg]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :nodes #{}}
             :b {:value "2" :nodes #{:a}}
             :c {:value "3" :nodes #{:b}}
             :d {:value "4" :nodes #{:c :b}}})

(deftest adjacent?-test
  (testing "Testing adjacent? function"
    (is (true? (cg/adjacent? g-test :b :a)))
    (is (false? (cg/adjacent? g-test :d :a)))))

(deftest neighbors-test
  (testing "Testing neighbors function"
    (is (= (cg/neighbors g-test :d) #{:b :c}))))

(deftest add-vertex-test
  (testing "Testing add-vertex function"
    (is (= (cg/add-vertex g-test :e "5" [:d])
           {:a {:value "1" :nodes #{}}
            :b {:value "2" :nodes #{:a}}
            :c {:value "3" :nodes #{:b}}
            :d {:value "4" :nodes #{:c :b}}
            :e {:value "5" :nodes #{:d}}}))))

(deftest remove-vertex-test
  (testing "Testing remove-vertex function"
    (is (= (cg/remove-vertex g-test :d)
           {:a {:value "1" :nodes #{}}
            :b {:value "2" :nodes #{:a}}
            :c {:value "3" :nodes #{:b}}}))))

(deftest add-edge-test
  (testing "Testing add-edge function"
    (is (= (cg/add-edge g-test :d :a)
           {:a {:value "1" :nodes #{}}
            :b {:value "2" :nodes #{:a}}
            :c {:value "3" :nodes #{:b}}
            :d {:value "4" :nodes #{:a :b :c}}}))))

(deftest remove-edge-test
  (testing "Testing remove-edge function"
    (is (= (cg/remove-edge g-test :d :b)
           {:a {:value "1" :nodes #{}}
            :b {:value "2" :nodes #{:a}}
            :c {:value "3" :nodes #{:b}}
            :d {:value "4" :nodes #{:c}}}))))

(deftest get-vertex-value
  (testing "Testing get-vertex-value function"
    (is (= (cg/get-vertex-value g-test :a) "1"))))

(deftest set-vertex-value
  (testing "Testing set-vertex-value function"
    (is (= (cg/set-vertex-value g-test :a "0")
           {:a {:value "0" :nodes #{}}
            :b {:value "2" :nodes #{:a}}
            :c {:value "3" :nodes #{:b}}
            :d {:value "4" :nodes #{:c :b}}}))))
