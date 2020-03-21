(map inc [1 2 3 ])
(map inc #{1 2 3})
(map inc '(1 2 3))

;(map inc 1)
;=> Error printing return value (IllegalArgumentException) at clojure.lang.RT/seqFrom (RT.java:557).
;=> Don't know how to create ISeq from: java.lang.Long

;Note: map function works with those that wrok with first, rest and cons


;; When map is passed more than one collection, the mapping function will
;; be applied until one of the collections runs out:

(map str ["a" "b" "c"] ["A" "B" "C"])
(map str ["a" "b" "c"] ["A" "B" ])
(map str ["a" "b" "c"] ["A" "B" "C"] [])


(def my-backpack [{:item "Books" :weight 1}
                  {:item "Laptop" :weight 2.7}
                  {:item "PowerBank" :weight 0.5}
                  {:item "Charger" :weight 0.2}
                  {:item "Lunchbox" :weight 0.8}])

;get list of each item's weight
(map :weight my-backpack)

;get total weight of backpack, including the weight of backpack itself 
(reduce + 0.5 (map :weight backpack))

;***
;Clojurists often use map to retrieve the value associated with a keyword
;from a collection of map data structures. Because keywords can be used as functions

;throught out the heavy items whose weight exceeds 2kg limit, 
(filter #(< (:weight %) 2) backpack) 



;Just be sure that your mapping function can take a number of arguments
;equal to the number of collections you’re passing to map.
(map inc [1 2 3 ] )

;(map inc [1 2 3] [1 2 3])
; => Error printing return value (ArityException) at clojure.lang.AFn/throwArity (AFn.java:429).
; => Wrong number of args (2) passed to: clojure.core/inc









  
  




