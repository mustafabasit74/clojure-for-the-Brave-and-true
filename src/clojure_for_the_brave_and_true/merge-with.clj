;; merge-with f & maps

(merge-with +
            {:a 2 :b 5}
            {:a 1 :b 10})
;; => {:a 3, :b 15}

(merge-with + 
            {:a 2 :b 4 :c 5}
            {:a 1}
            {:a 2 :b 3 :c 10 :d 3})
;; => {:a 5, :b 7, :c 15, :d 3}

(merge-with + 
            {:a 3 :b 3}
            {:id 10 :age 25})
;; => {:a 3, :b 3, :id 10, :age 25}

(merge-with max
            {:a 2 :b 4 :c 5}
            {:a 1}
            {:a 2 :b 3 :c 10 :d 3})
;; => {:a 2, :b 4, :c 10, :d 3}

;; Demonstrating difference between merge and merge-with
;; For merge the value from the right-most map wins:
(merge  {:a 1} {:a 2} {:a 3})
;; => {:a 3}

;; while for merge-with values are merged (with function + in this example):
(merge-with + {:a 1} {:a 2} {:a 3})
;; => {:a 6}

;; https://clojuredocs.org/clojure.core/merge-with#example-55181fece4b08eb9aa0a8d3c




