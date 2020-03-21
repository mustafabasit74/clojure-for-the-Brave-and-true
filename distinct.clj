(distinct [1 2 2  3 3 3 4 4 4 4])
(distinct nil)
(distinct [])
(distinct [nil nil])


;square
(map #(* % %) (distinct [1 2 2 3 3 ]))

;sum of distinct elements
(reduce #(+ %1 %2)  0 (distinct [1 2 3 2]) )



