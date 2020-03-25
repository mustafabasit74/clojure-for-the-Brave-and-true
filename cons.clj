;cons - short for construct
(cons 1 '(2 3 4))
(cons 1 [2 3 4])

;; ***
(cons [1 2 3] [4 5 6])
(cons [1 2 ] '(4 5 6))

;converts a map to vector
(cons {:a 1 :b 2 } {:c 3})
;; => ({:a 1, :b 2} [:c 3])


;; converts the second argument to a seq, prepends the first argument to it, regardless
;; of whether it was in the set or not
(cons 2 #{1 2 3})
;; => (2 1 3 2)

(cons 2 [1 2 3])
;; => (2 1 3 2)



(cons 3 nil)
;; => (3)


;; "cons" does not realize second parameter, 
;;    opening the world for recursive functions that create lazy sequences

;; still confusion, see on clojure docs



