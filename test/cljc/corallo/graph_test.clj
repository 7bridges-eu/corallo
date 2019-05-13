(ns corallo.graph-test
  (:require [corallo.graph :as cg]
            [clojure.test :refer :all]))

(def g-test {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                        :b {:value "2" :in #{:a} :out #{:c}}
                        :c {:value "3" :in #{:b} :out #{:d}}
                        :d {:value "4" :in #{:c} :out #{}}}
             :edges {[:a :b] {:p1 :test}
                     [:b :c] {}
                     [:c :d] {}}})

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
           {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                       :b {:value "2" :in #{:a} :out #{:c}}
                       :c {:value "3" :in #{:b} :out #{:d}}
                       :d {:value "4" :in #{:c} :out #{}}
                       :e {:value "5" :in #{} :out #{}}}
            :edges {[:a :b] {:p1 :test}
                    [:b :c] {}
                    [:c :d] {}}}))))

(deftest remove-vertex-test
  (testing "Testing remove-vertex function"
    (is (= (cg/remove-vertex g-test :d)
           {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                       :b {:value "2" :in #{:a} :out #{:c}}
                       :c {:value "3" :in #{:b} :out #{}}}
            :edges {[:a :b] {:p1 :test}
                    [:b :c] {}}}))))

(deftest add-edge-test
  (testing "Testing add-edge function"
    (is
     (= (cg/add-edge g-test :d :a)
        {:vertexes {:a {:value "1" :in #{:d} :out #{:b}}
                    :b {:value "2" :in #{:a} :out #{:c}}
                    :c {:value "3" :in #{:b} :out #{:d}}
                    :d {:value "4" :in #{:c} :out #{:a}}}
         :edges {[:a :b] {:p1 :test} [:b :c] {} [:c :d] {} [:d :a] {}}}))
    (is
     (= (cg/add-edge g-test :d :a {:p1 :test})
        {:vertexes {:a {:value "1" :in #{:d} :out #{:b}}
                    :b {:value "2" :in #{:a} :out #{:c}}
                    :c {:value "3" :in #{:b} :out #{:d}}
                    :d {:value "4" :in #{:c} :out #{:a}}}
         :edges {[:a :b] {:p1 :test}
                 [:b :c] {}
                 [:c :d] {}
                 [:d :a] {:p1 :test}}}))))

(deftest remove-edge-test
  (testing "Testing remove-edge function"
    (is (= (cg/remove-edge g-test :b :c)
           {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                       :b {:value "2" :in #{:a} :out #{}}
                       :c {:value "3" :in #{} :out #{:d}}
                       :d {:value "4" :in #{:c} :out #{}}}
            :edges {[:a :b] {:p1 :test}
                    [:c :d] {}}}))))

(deftest get-vertex-value-test
  (testing "Testing get-vertex-value function"
    (is (= (cg/get-vertex-value g-test :a) "1"))))

(deftest set-vertex-value-test
  (testing "Testing set-vertex-value function"
    (is (= (cg/set-vertex-value g-test :a "0")
           {:vertexes {:a {:value "0" :in #{} :out #{:b}}
                       :b {:value "2" :in #{:a} :out #{:c}}
                       :c {:value "3" :in #{:b} :out #{:d}}
                       :d {:value "4" :in #{:c} :out #{}}}
            :edges {[:a :b] {:p1 :test}
                    [:b :c] {}
                    [:c :d] {}}}))))

(deftest get-edge-properties-test
  (testing "Testing get-edge-properties function"
    (is (= (cg/get-edge-properties g-test :a :b) {:p1 :test}))))

(deftest set-edge-properties
  (testing "Testing set-edge-properties function"
    (is (= (cg/set-edge-properties g-test :a :b {:p1 nil})
           {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                       :b {:value "2" :in #{:a} :out #{:c}}
                       :c {:value "3" :in #{:b} :out #{:d}}
                       :d {:value "4" :in #{:c} :out #{}}}
            :edges {[:a :b] {:p1 nil}
                    [:b :c] {}
                    [:c :d] {}}}))))
