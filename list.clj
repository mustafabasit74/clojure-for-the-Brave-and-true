'(1 2 3 4 5)
(nth '(1 2 3 4 5 ) 0 )
(list 1 2 3 4 5  )
(list 1 [2 3] {:four 4 :five 5 } )

(conj '(1 2 3 4 ) 5 )


;; it’s good to know that using nth to retrieve an element from a list
;; is slower than using get to retrieve an element from a vector.


