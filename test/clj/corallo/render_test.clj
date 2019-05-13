(ns corallo.render-test
  (:require [clojure.test :refer :all]
            [corallo.render :as render]
            [tangle.core :as t]))

(def g-test {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                        :b {:value "2" :in #{:a} :out #{:c}}
                        :c {:value "3" :in #{:b} :out #{:d}}
                        :d {:value "4" :in #{:c} :out #{}}}
             :edges {[:a :b] {}
                     [:b :c] {}
                     [:c :d] {}}})

(deftest render-graph-test
  (testing "Testing render-graph function"
    (let [f #'render/render-graph]
      (with-redefs [t/graph->dot (fn [n e opts] n)
                    t/dot->image (fn [d ext] d)]
        (is (= (f g-test) [:a :b :c :d]))))))
