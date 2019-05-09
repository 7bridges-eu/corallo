<p align="center">
    <img src="https://7bridges.eu/img/logo-inline.png" alt="7bridges clj-odbp"
    width="500px" height="122px"/>
</p>

# corallo

A tiny graph library in Clojure.

[![Clojars Project](https://img.shields.io/clojars/v/eu.7bridges/corallo.svg)](https://clojars.org/eu.7bridges/corallo)

## Usage

A graph in `corallo` is a Clojure map. For instance:

``` clojure
(def g {:vertexes {:a {:value "1" :in #{} :out #{:b}}
                   :b {:value "2" :in #{:a} :out #{:c}}
                   :c {:value "3" :in #{:b} :out #{:d}}
                   :d {:value "4" :in #{:c} :out #{}}}
        :edges {[:a :b] {}
                [:b :c] {}
                [:c :d] {}}})
```

You can manipulate the graph by adding vertexes:

``` clojure
user> (require '[corallo.graph :as graph])
user> (graph/add-vertex g :e "5")
{:vertexes {:a {:value "1", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{:d}},
            :d {:value "4", :in #{:c}, :out #{}},
            :e {:value "5", :in #{}, :out #{}}},
 :edges {[:a :b] {}, [:b :c] {}, [:c :d] {}}}
```

Or you can add an edge between two vertexes:

``` clojure
user> (-> (graph/add-vertex g :e "5")
          (graph/add-edge :d :e))
{:vertexes {:a {:value "1", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{:d}},
            :d {:value "4", :in #{:c}, :out #{:e}},
            :e {:value "5", :in #{:d}, :out #{}}},
 :edges {[:a :b] {}, [:b :c] {}, [:c :d] {}, [:d :e] {}}}
```

You can set the value of a vertex after the graph has been created:

``` clojure
user> (graph/set-vertex-value g :a "0")
{:vertexes {:a {:value "0", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{:d}},
            :d {:value "4", :in #{:c}, :out #{}}},
 :edges {[:a :b] {}, [:b :c] {}, [:c :d] {}}}
```

Or you can remove a vertex:

``` clojure
user> (graph/remove-vertex g :d)
{:vertexes {:a {:value "1", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{}}},
 :edges {[:a :b] {}, [:b :c] {}}}
```

The operations in `corallo.graph` leverage Clojure immutability, so they return
a new graph instead of modifying the original one.

Edges can have properties:

``` clojure
user> (-> (graph/add-vertex g :e "5")
          (graph/add-edge :d :e {:p1 "a property"}))
{:vertexes {:a {:value "1", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{:d}},
            :d {:value "4", :in #{:c}, :out #{:e}},
            :e {:value "5", :in #{:d}, :out #{}}},
 :edges {[:a :b] {}, [:b :c] {}, [:c :d] {}, [:d :e] {:p1 "a property"}}}

user> (-> (graph/add-vertex g :e "5")
          (graph/add-edge :d :e)
          (graph/set-edge-properties :a :b {:p1 "a property"}))
{:vertexes {:a {:value "1", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{:d}},
            :d {:value "4", :in #{:c}, :out #{:e}},
            :e {:value "5", :in #{:d}, :out #{}}},
 :edges {[:a :b] {:p1 "a property"}, [:b :c] {}, [:c :d] {}, [:d :e] {}}}
```

Like vertexes, edges can be removed from the graph:

``` clojure
user> (graph/remove-edge g :c :d)
{:vertexes {:a {:value "1", :in #{}, :out #{:b}},
            :b {:value "2", :in #{:a}, :out #{:c}},
            :c {:value "3", :in #{:b}, :out #{}},
            :d {:value "4", :in #{}, :out #{}}},
 :edges {[:a :b] {}, [:b :c] {}}}
```

You can query the graph for information:

``` clojure
user> (graph/neighbors g :b)
#{:c :a}

user> (graph/adjacent? g :a :c)
false
user> (graph/adjacent? g :b :c)
true
```

The graph can be topologically sorted:

``` clojure
user> (require '[corallo.operations :as operations])
user> (operations/topo-sort g)
(:a :b :c :d)
```

And you can verify if the graph is acyclic and complete:

``` clojure
user> (operations/acyclic-graph? g)
true
user> (-> (graph/add-edge g :d :a)
          operations/acyclic-graph?)
false

user> (operations/complete-graph? g)
true
user> (-> (graph/remove-edge g :b :c)
          operations/complete-graph?)
false
```

Finally, the graph can be rendered as a PNG file thanks to
[tangle](https://github.com/Macroz/tangle):

``` clojure
user> (operations/graph->png g "/tmp/graph.png")
nil
```

And this is the resulting image for the graph `g`:

<p align="center">
    <img src="https://github.com/7bridges-eu/corallo/blob/master/resources/graph.png"/>
</p>

Note that you can also get the byte array representing the graph image:

``` clojure
user> (operations/graph->byte-array g)
[-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 86, 0,
 0, 1, 105, 8, 2, 0, 0, 0, -119, 111, 72, 108, 0, 0, 0, 6, 98, 75, 71, 68, 0,
 -1, 0, -1, 0, -1, -96, -67, -89, ...]
```

## License
Copyright © 2019 7bridges s.r.l.

Distributed under the Apache License 2.0.
