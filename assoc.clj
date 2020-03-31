(assoc {} :key1 "value1")
;; => {:key1 "value1"}

(assoc nil :key1 "value1")
;; => {:key1 "value1"}
;; nil is being treated as an empty map

;; ***
(assoc {} nil nil)
;;=> {nil nil}

(assoc {} false false)
;;=> {false false}

(assoc {} :key1 "value1" :key2 "value2")
;; => {:key1 "value1", :key2 "value2"}

(assoc {:a 1} 1 2 3 4 5 6)
;; => {:a 1, 1 2, 3 4, 5 6}

(assoc {:basit "7006885555"} :basit "9682555555")
;; => {:basit "9682555555"}

;; 'assoc' can be used on a vector (but not a list), in this way: 
;; (assoc vec index replacement)

(assoc [7 8 9 10] 2) 
;; => Execution error (ArityException) at user/eval19 (REPL:1).
;; => Wrong number of args (2) passed to: clojure.core/assoc--5416


(assoc [7 8 9 10] 0 0)
;; => [0 8 9 10]

(assoc [7 8 9 10] 0 [1 2 3])
(assoc [7 8 9 10] 3 '(1 2 3))

;; ***
(assoc [7 8 9 5] 3 10)
;; =>  [7 8 9 10]

;;  length of vector = 4 (count vector), last element's index = 3
(assoc [7 8 9 5] 4 1000)
;; => [7 8 9 10 1000] -- if index = (count vector ), it will append the element

;; if index > (count vector), error will be thrown
(assoc [7 8 9 5] 5 10)
;; => Execution error (IndexOutOfBoundsException) at user/eval5 (REPL:1).
;; => null


(assoc {} [[:a 1] [:b 2]])