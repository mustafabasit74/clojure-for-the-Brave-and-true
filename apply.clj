;; (apply F [1 2 3 4 5]) translates to (F 1 2 3 4 5)
;; (map F [1 2 3 4 5])   translates to [(F 1) (F 2) (F 3) (F 4) (F 5)]


;; Apply - only once function will be called 

;; ***
;; apply breaks a seqable data structure into pieces so it can be passed to a function that
;; expects a rest parameter

(max 0 2 2 34 4 4)

(apply max [1 2 2 3 4 5 5 6])

;; use apply, if the number of arguments to pass to the function is not known at compile-time

(map first [[1 4 9] [2 9 4] [0 17 3]])

(apply first [[1 4 9] [2 9 4] [0 17 3]])
;; => Execution error (ArityException) at user/eval15 (REPL:1).
;; => Wrong number of args (3) passed to: clojure.core/first--5384
;; it's doing something like this (first [1 4 9] [2 9 4] [0 17 3] )

(map first [[1 4 9] [2 9 4] [0 17 3]])





