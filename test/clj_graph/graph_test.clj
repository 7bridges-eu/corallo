(ns clj-graph.graph-test
  (:require [clj-graph.graph :as cg]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :in #{} :out #{:b}}
             :b {:value "2" :in #{:a} :out #{:c}}
             :c {:value "3" :in #{:b} :out #{:d}}
             :d {:value "4" :in #{:c} :out #{}}})

(deftest adjacent?-test
  (testing "Testing adjacent? function"
    (is (true? (cg/adjacent? g-test :b :a)))
    (is (false? (cg/adjacent? g-test :d :a)))))

(deftest neighbors-test
  (testing "Testing neighbors function"
    (is (= (cg/neighbors g-test :c) #{:b :d}))))

(deftest add-vertex-test
  (testing "Testing add-vertex function"
    (is (= (cg/add-vertex g-test :e "5")
           {:a {:value "1" :in #{} :out #{:b}}
            :b {:value "2" :in #{:a} :out #{:c}}
            :c {:value "3" :in #{:b} :out #{:d}}
            :d {:value "4" :in #{:c} :out #{}}
            :e {:value "5" :in #{} :out #{}}}))))

(deftest remove-vertex-test
  (testing "Testing remove-vertex function"
    (is (= (cg/remove-vertex g-test :d)
           {:a {:value "1" :in #{} :out #{:b}}
            :b {:value "2" :in #{:a} :out #{:c}}
            :c {:value "3" :in #{:b} :out #{}}}))))

(deftest add-edge-test
  (testing "Testing add-edge function"
    (is (= (cg/add-edge g-test :d :a)
           {:a {:value "1" :in #{:d} :out #{:b}}
            :b {:value "2" :in #{:a} :out #{:c}}
            :c {:value "3" :in #{:b} :out #{:d}}
            :d {:value "4" :in #{:c} :out #{:a}}}))))

(deftest remove-edge-test
  (testing "Testing remove-edge function"
    (is (= (cg/remove-edge g-test :b :c)
           {:a {:value "1" :in #{} :out #{:b}}
            :b {:value "2" :in #{:a} :out #{}}
            :c {:value "3" :in #{} :out #{:d}}
            :d {:value "4" :in #{:c} :out #{}}}))))

(deftest get-vertex-value
  (testing "Testing get-vertex-value function"
    (is (= (cg/get-vertex-value g-test :a) "1"))))

(deftest set-vertex-value
  (testing "Testing set-vertex-value function"
    (is (= (cg/set-vertex-value g-test :a "0")
           {:a {:value "0" :in #{} :out #{:b}}
            :b {:value "2" :in #{:a} :out #{:c}}
            :c {:value "3" :in #{:b} :out #{:d}}
            :d {:value "4" :in #{:c} :out #{}}}))))
