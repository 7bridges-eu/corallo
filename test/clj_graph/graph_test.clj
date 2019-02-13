(ns clj-graph.graph-test
  (:require [clj-graph.graph :as cg]
            [clojure.test :refer :all]))

(def g-test {:a {:value "1" :in #{} :out #{{:b {:p1 :test}}}}
             :b {:value "2" :in #{{:a {:p1 :test}}} :out #{{:c {:p1 :test}}}}
             :c {:value "3" :in #{{:b {:p1 :test}}} :out #{{:d {:p1 :test}}}}
             :d {:value "4" :in #{{:c {:p1 :test}}} :out #{}}})

(deftest adjacent?-test
  (testing "Testing adjacent? function"
    (is (true? (cg/adjacent? g-test :b :a)))
    (is (false? (cg/adjacent? g-test :d :a)))))

(deftest neighbors-test
  (testing "Testing neighbors function"
    (is (= (cg/neighbors g-test :c) #{{:b {:p1 :test}} {:d {:p1 :test}}}))))

(deftest add-vertex-test
  (testing "Testing add-vertex function"
    (is (= (cg/add-vertex g-test :e "5")
           {:a {:value "1" :in #{} :out #{{:b {:p1 :test}}}}
            :b {:value "2" :in #{{:a {:p1 :test}}} :out #{{:c {:p1 :test}}}}
            :c {:value "3" :in #{{:b {:p1 :test}}} :out #{{:d {:p1 :test}}}}
            :d {:value "4" :in #{{:c {:p1 :test}}} :out #{}}
            :e {:value "5" :in #{} :out #{}}}))))

(deftest remove-vertex-test
  (testing "Testing remove-vertex function"
    (is (= (cg/remove-vertex g-test :d)
           {:a {:value "1" :in #{} :out #{{:b {:p1 :test}}}}
            :b {:value "2" :in #{{:a {:p1 :test}}} :out #{{:c {:p1 :test}}}}
            :c {:value "3" :in #{{:b {:p1 :test}}} :out #{}}}))))

(deftest add-edge-test
  (testing "Testing add-edge function"
    (is
     (= (cg/add-edge g-test :d :a)
        {:a {:value "1" :in #{{:d {}}} :out #{{:b {:p1 :test}}}}
         :b {:value "2" :in #{{:a {:p1 :test}}} :out #{{:c {:p1 :test}}}}
         :c {:value "3" :in #{{:b {:p1 :test}}} :out #{{:d {:p1 :test}}}}
         :d {:value "4" :in #{{:c {:p1 :test}}} :out #{{:a {}}}}}))
    (is
     (= (cg/add-edge g-test :d :a {:p1 :test})
        {:a {:value "1" :in #{{:d {:p1 :test}}} :out #{{:b {:p1 :test}}}}
         :b {:value "2" :in #{{:a {:p1 :test}}} :out #{{:c {:p1 :test}}}}
         :c {:value "3" :in #{{:b {:p1 :test}}} :out #{{:d {:p1 :test}}}}
         :d {:value "4" :in #{{:c {:p1 :test}}} :out #{{:a {:p1 :test}}}}}))))

(deftest remove-edge-test
  (testing "Testing remove-edge function"
    (is (= (cg/remove-edge g-test :b :c)
           {:a {:value "1" :in #{} :out #{{:b {:p1 :test}}}}
            :b {:value "2" :in #{{:a {:p1 :test}}} :out #{}}
            :c {:value "3" :in #{} :out #{{:d {:p1 :test}}}}
            :d {:value "4" :in #{{:c {:p1 :test}}} :out #{}}}))))

(deftest get-vertex-value
  (testing "Testing get-vertex-value function"
    (is (= (cg/get-vertex-value g-test :a) "1"))))

(deftest set-vertex-value
  (testing "Testing set-vertex-value function"
    (is (= (cg/set-vertex-value g-test :a "0")
           {:a {:value "0" :in #{} :out #{{:b {:p1 :test}}}}
            :b {:value "2" :in #{{:a {:p1 :test}}} :out #{{:c {:p1 :test}}}}
            :c {:value "3" :in #{{:b {:p1 :test}}} :out #{{:d {:p1 :test}}}}
            :d {:value "4" :in #{{:c {:p1 :test}}} :out #{}}}))))
