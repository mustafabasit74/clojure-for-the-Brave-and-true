;; You use doseq when you want to perform side-effecting operations (like
;; printing to a terminal) on the elements of a collection. The vector that
;; immediately follows the name doseq describes how to bind all the elements
;; in a collection to a name one at a time so you can operate on them.


(doseq [x [1 2 3 4]]
  (println x))

(doseq [x [1 2 3 4]
        y [55 66]]
  (println x y))

;; it's like nested for loop
;; Repeatedly executes body (presumably for side-effects) with
;; bindings and filtering as provided by "for" .  Does not retain
;; the head of the sequence. Returns nil

(doseq [x [1 2 3 4]
        y [55 66]
        z [700 800]]
  (println x y z))


;; Multiplies every x by every y.
(doseq [x [-1 0 1]
        y [1  2 3]]
  (prn (* x y)))
-1
-2
-3
0
0
0
1
2
3
nil

;; Notice that the x iterates more slowly than y.

(list 1 2)
;; => (1 2)

(list 1 2 3 4 5)
;; => (1 2 3 4 5)

(doseq [[first second & remaining] (list [1 2 5 3 8 4 ] [1 2 0 0 0 0 0 0] [1 2 5 5 5 5 5 5 5 ])]
  (println first second remaining))


(doseq [[x y] (map list 
                   [1 2 3 4 5] 
                   [6 7 8] 
                   [1 1 1 1 1 1 1 1 1 1 1 1 1 1])]
  (println (+ x y)))
;; => 7 
;; => 9
;; => 11 
;; => nil
;; last vector is not considered - see destructuring above


;; ***
(doseq [[[k1 v1] [k2 v2]] (map list 
                               (sorted-map :1 1 :5 5 :2 2) 
                               (sorted-map :4 4 :2 2 :0 0))]
       (println (+ v1 v2)))
;; => 1
;; => 4
;; => 9
;; => nil


