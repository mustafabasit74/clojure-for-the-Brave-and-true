;; Returns true if x is logical false, false otherwise.

(not false)
(not (= 1 2))
(not nil)

(not true)
(not 0)
(not 1)
(not 2)
(not "hello")
(not +)


(filter even? [1 2 3 4 5 6 7])
(filter #(even? %) [1 2 3 4 5 6 7])

(filter #(not (even? %) ) [1 2 3 4 5 6 7])


